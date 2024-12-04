package com.diagnostico_service.service;

import com.diagnostico_service.dto.AssingSupervisorDTO;
import com.diagnostico_service.dto.CompanyDateFormDTO;
import com.diagnostico_service.dto.UsuarioResponseDTO;
import com.diagnostico_service.entities.Categoria;
import com.diagnostico_service.entities.Usuario;
import com.diagnostico_service.enums.EstadoUsuario;
import com.diagnostico_service.enums.Role;
import com.diagnostico_service.exceptions.BadUserCredentialsException;
import com.diagnostico_service.exceptions.ObjectNotFoundException;
import com.diagnostico_service.helpers.MensajeServicios;
import com.diagnostico_service.maps.IMapUsuario;
import com.diagnostico_service.projection.ReportDTO;
import com.diagnostico_service.repository.*;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.AreaBreakType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.io.*;
import java.net.MalformedURLException;
import java.util.*;

@Service
public class UsuarioServiceImpl {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IMapUsuario mapUsuario;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioPagRepository usuarioPagRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObservacionRepository observacionRepository;

    @Autowired
    private PuntuacionTotalRepository puntuacionTotalRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    public Page<UsuarioResponseDTO> getAllUsuarios(Integer page, Integer elements, String sortDirection, String sortBy) throws NullPointerException {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortBy);
        Pageable pageable = PageRequest.of(page, elements,sort);
        Page<Usuario> usuariosPage = usuarioPagRepository.findAll(pageable);
        return usuariosPage.map(this::convertToDto);
    }

    public UsuarioResponseDTO getUsuario(String authorizationHeader, Integer idUsuario) throws NullPointerException {
        Usuario usuario;
        if (idUsuario == null) {
            usuario = jwtService.getUsuarioFromAuthorizationHeader(authorizationHeader);
        }else {
            usuario = usuarioRepository.findById(idUsuario).get();
        }
        return mapUsuario.mapUsuario(usuario);
    }

    public UsuarioResponseDTO convertToDto(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreCompleto(usuario.getNombreCompleto())
                .cargo(usuario.getCargo())
                .estadoUsuario(usuario.getEstadoUsuario())
                .aniosVinculado(usuario.getAniosVinculado())
                .role(usuario.getRole())
                .supervisor(usuario.getSupervisor() != null ? usuario.getSupervisor().getNombreCompleto() : null)
                .empresa(usuario.getEmpresa() != null ? usuario.getEmpresa().getNombreEmpresa() : null) // Ajusta según el nombre del campo en Empresa
                .username(usuario.getUsername())
                .formTerminado(usuario.getFormTerminado())
                .build();
    }

    public Page<UsuarioResponseDTO> searchUsuarios(String query, Integer page, Integer elements,  String sortDirection, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),sortBy);
        Pageable pageable = PageRequest.of(page, elements,sort);
        Page<Usuario> usuariosPage = usuarioPagRepository.findByNombreCompletoContainingOrUsernameContaining(query, query, pageable);
        return usuariosPage.map(this::convertToDto);
    }

    public UsuarioResponseDTO updateUsuario(UsuarioResponseDTO usuarioResponseDTO) throws ObjectNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuarioResponseDTO.getIdUsuario());
        if (usuarioOptional.isEmpty()){
            throw new ObjectNotFoundException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        if (usuarioResponseDTO.getRole() != null){
            Role rolActual = usuarioRepository.findRoleByIdUsuario(usuarioResponseDTO.getIdUsuario());
            if (!rolActual.equals(usuarioResponseDTO.getRole())){
                usuario.setRole(usuarioResponseDTO.getRole());
            }
        }

        if (usuarioResponseDTO.getFormTerminado() != null){
            Boolean formTerminadoActual = usuario.getFormTerminado();
            if (!formTerminadoActual.equals(usuarioResponseDTO.getFormTerminado())){
                usuario.setFormTerminado(usuarioResponseDTO.getFormTerminado());
            }
        }

        try {
            if (usuarioResponseDTO.getEstadoUsuario() != null){
                EstadoUsuario estadoUsuarioActual = usuario.getEstadoUsuario();
                if (!estadoUsuarioActual.equals(usuarioResponseDTO.getEstadoUsuario())){
                    if (estadoUsuarioActual.equals(EstadoUsuario.Inactivo) && usuarioResponseDTO.getEstadoUsuario().equals(EstadoUsuario.Activo)){
                        usuario.setEstadoUsuario(usuarioResponseDTO.getEstadoUsuario());
                        String correo = usuario.getUsername();
                        String subject = "Activación de cuenta en el diagnóstico Temptech";
                        String body = "Su cuenta ha sido activada exitosamente. Ahora puede acceder al sitio web y realizar su diagnóstico.";
                        mailService.sendEmail(correo, subject, body);
                    }else {
                        usuario.setEstadoUsuario(usuarioResponseDTO.getEstadoUsuario());
                    }
                }
            }

        }catch (Exception e){
            throw new BadUserCredentialsException(e.getMessage());
        }

        return mapUsuario.mapUsuario(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> assignSupervisor(AssingSupervisorDTO assingSupervisorDTO) throws ObjectNotFoundException {
        if (assingSupervisorDTO.getIdClienteList().isEmpty()){
            throw new BadUserCredentialsException("No hay clientes para asignar al supervisor");
        }
        Optional<Usuario> supervisorOptional = usuarioRepository.findById(assingSupervisorDTO.getIdSupervisor());
        if (supervisorOptional.isEmpty()){
            throw new ObjectNotFoundException("Supervisor no encontrado");
        }
        Usuario supervisor = supervisorOptional.get();
        List<UsuarioResponseDTO> usuarioResponseDTOList = new ArrayList<>();
        for (Integer idUsuario : assingSupervisorDTO.getIdClienteList()) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
            if (usuarioOptional.isEmpty()){
                throw new ObjectNotFoundException("El usuario no existe");
            }
            Usuario usuario = usuarioOptional.get();
            if (usuario.getSupervisor() != null){
                throw new BadUserCredentialsException("El usuario ya tiene supervisor");
            }
            usuario.setSupervisor(supervisor);
            usuarioResponseDTOList.add(
                    mapUsuario.mapUsuario(usuarioRepository.save(usuario))
            );
        }
        return usuarioResponseDTOList;
    }

    public Boolean deleteSupervisorClients(AssingSupervisorDTO assingSupervisorDTO) throws ObjectNotFoundException {
        Optional<Usuario> supervisorOptional = usuarioRepository.findById(assingSupervisorDTO.getIdSupervisor());
        if (supervisorOptional.isEmpty()){
            throw new ObjectNotFoundException("Supervisor no encontrado");
        }
        Usuario supervisor = supervisorOptional.get();
        if (!supervisor.getRole().equals(Role.SUPERVISOR)){
            throw new BadUserCredentialsException("El usuario no es supervisor");
        }
        if (assingSupervisorDTO.getIdClienteList().isEmpty()){
            throw new ObjectNotFoundException("No hay clientes para eliminar del supervisor");
        }
        for (Integer idUsuario : assingSupervisorDTO.getIdClienteList()) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
            if (idUsuario.equals(supervisor.getIdUsuario())){
                throw new BadUserCredentialsException("El usuario es igual al supervisor");
            }
            if (usuarioOptional.isEmpty()){
                throw new ObjectNotFoundException("El usuario no existe");
            }
            Usuario usuario = usuarioOptional.get();
            if (usuario.getSupervisor() != null){
                if (!usuario.getSupervisor().equals(supervisor)){
                    throw new BadUserCredentialsException("El usuario no es del supervisor " + supervisor.getNombreCompleto());
                }
                usuario.setSupervisor(null);
                usuarioRepository.save(usuario);
            }else {
                throw new BadUserCredentialsException("El usuario no tiene supervisor");
            }
        }
        return true;
    }

    public Map<String, Object> isFormularioFinished(String authorizationHeader){
        String token = authorizationHeader.substring(7);
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(jwtService.extractUsername(token));
        if (usuarioOptional.isEmpty()){
            throw new BadUserCredentialsException("El usuario no existe");
        }
        Map<String,Object> response = new HashMap<>();
        Usuario usuarioFinal = usuarioOptional.get();
        if (usuarioFinal.getFormTerminado()){
            response.put("isFinished",true);
        }else {
            response.put("isFinished",false);
        }
        return response;
    }

    public List<UsuarioResponseDTO> findAllUsuariosWithoutSupervisor(){
        return mapUsuario.mapUsuarioList(usuarioRepository.findAllUsersWithoutSupervisor());
    }

    public List<UsuarioResponseDTO> findClientsOfSupervisor(String headers, Integer idSupervisor){
        Integer supervisorId;
        Optional<Usuario> supervisorOptional;
        if (idSupervisor != null){
            supervisorId = idSupervisor;
            supervisorOptional = usuarioRepository.findById(supervisorId);
        }else {
            String token = headers.substring(7);
            supervisorOptional = usuarioRepository.findByUsername(jwtService.extractUsername(token));
        }
        if (supervisorOptional.isEmpty()){
            throw new BadUserCredentialsException("El usuario no existe");
        }
        Usuario usuario = supervisorOptional.get();
        Role role = usuarioRepository.findRoleByIdUsuario(usuario.getIdUsuario());
        if (role.equals(Role.CLIENTE)){
            throw new BadUserCredentialsException("El usuario no es supervisor");
        }
        return mapUsuario.mapUsuarioList(usuarioRepository.findAllClientsOfSupervisor(usuario.getIdUsuario()));
    }

    public Map<String, Object> csvResults() throws IOException {

        List<Categoria> categoriaList = categoriaRepository.findAll();

        Workbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true); // Negritas
        headerStyle.setFont(headerFont);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex()); // Color de fondo
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        List<CompanyDateFormDTO> empresaList = empresaRepository.findEmpresasFormulario();
        for (Categoria categoria : categoriaList) {
            List<String> headerList = new ArrayList<>();
            Integer idCategoria = categoria.getIdCategoria();
            headerList.add(0,"Nit Empresa");
            headerList.add(1,"Fecha aplicacion");
            headerList.addAll(preguntaRepository.findNombrePregunta(categoria.getIdCategoria()));
            headerList.add("Observacion");
            Sheet sheet = workbook.createSheet(categoria.getNombreCategoria());
            Row headerRow = sheet.createRow(0);
            for (int j = 0; j < headerList.size(); j++) {
                Cell cell = headerRow.createCell(j);
                cell.setCellValue(headerList.get(j));
                cell.setCellStyle(headerStyle); // Aplicar el estilo a la celda
                sheet.autoSizeColumn(j);
            }
            for (int j = 1; j < empresaList.size()+1; j++) {
                String nitEmpresa = empresaList.get(j-1).getNitEmpresa();
                Date fecha = empresaList.get(j-1).getFechaAplicacion();
                List<ReportDTO> reportDTOList = usuarioRepository.reportData(nitEmpresa, fecha,idCategoria);
                Row row = sheet.createRow(j);
                List<String> reportStrings = reportDTOList.stream()
                        .map(ReportDTO::getRespuesta)
                        .toList();
                List<String> registers = new ArrayList<>();
                registers.add(nitEmpresa);
                registers.add(fecha.toString());
                registers.addAll(reportStrings);
                String observacion = observacionRepository.obtenerTextoObservacion(nitEmpresa,categoria.getIdCategoria(),fecha);
                registers.add(observacion);
                for (int h = 0; h < registers.size(); h++) {
                    row.createCell(h).setCellValue(registers.get(h));
                    sheet.autoSizeColumn(h);

                }
            }
        }
        // Configurar la respuesta HTTP para la descarga del archivo
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();
            workbook.close();
            Map<String,Object> respuesta = new HashMap<>();
            respuesta.put("respuesta",Base64.getEncoder().encodeToString(excelBytes));
            return respuesta;
        }
    }

    public List<ReportDTO> csvResultsData(String nitEmpresa, Date date, Integer idCategoria) {
        return usuarioRepository.reportData(nitEmpresa,date,idCategoria);
    }

    public void sendFinishedFormEmail(InputStream graficaImageStream, InputStream termometroImageStream, String authorizationHeader, Integer idUsuario){
        Usuario usuario;
        if (idUsuario == null){
            usuario = jwtService.getUsuarioFromAuthorizationHeader(authorizationHeader);
        }else {
            usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new BadUserCredentialsException("El usuario no existe"));
        }
        String nitEmpresa = usuario.getEmpresa().getNitEmpresa();
        Date date = formularioRepository.findLastFecha(nitEmpresa);
        String toUser = usuario.getUsername();
        String subject = MensajeServicios.ASUNTO_FORM_TERMINADO.getMensaje();
        Double promedioPuntuacionTotal = puntuacionTotalRepository.findAverageTotalByNitEmpresaAndFecha(nitEmpresa, date);
        if (promedioPuntuacionTotal == null){
            throw new BadUserCredentialsException("El usuario no tiene puntuaciones totales");
        }
        String message = "";
        if (promedioPuntuacionTotal > 100) {
            promedioPuntuacionTotal = 100d;
        }
        if (promedioPuntuacionTotal >= 0 && promedioPuntuacionTotal <= 20) {
            message = MensajeServicios.MENSAJE_NIVEL_1.getMensaje();
        }
        if (promedioPuntuacionTotal > 20 && promedioPuntuacionTotal <= 40) {
            message = MensajeServicios.MENSAJE_NIVEL_2.getMensaje();
        }
        if (promedioPuntuacionTotal > 40 && promedioPuntuacionTotal <= 60) {
            message = MensajeServicios.MENSAJE_NIVEL_3.getMensaje();
        }
        if (promedioPuntuacionTotal > 60 && promedioPuntuacionTotal <= 80) {
            message = MensajeServicios.MENSAJE_NIVEL_4.getMensaje();
        }
        if (promedioPuntuacionTotal > 80) {
            message = MensajeServicios.MENSAJE_NIVEL_5.getMensaje();
        }
        File pdf = generatePdf(graficaImageStream,termometroImageStream, MensajeServicios.NOMBRE_ARCHIVO_RESULTADOS_PDF.getMensaje() + " " + usuario.getEmpresa().getNombreEmpresa());

        File headerImage = new File("src/main/java/com/diagnostico_service/helpers/TemptechHeader.jpg");
        File footerImage = new File("src/main/java/com/diagnostico_service/helpers/TemptechFooter.jpg");

        String body = String.format(
                "<div style='font-family: Arial, sans-serif; padding: 20px;'>" +
                        "<p>Estimado %s,</p>" +
                        "<p>Adjunto a este correo encontrará el <span style='color: #0138a1; font-weight: bold;'>informe detallado del diagnóstico de madurez digital</span> realizado mediante nuestros formularios en línea. Estos resultados presentan una evaluación exhaustiva del nivel de madurez digital de su organización, basada en las respuestas proporcionadas.</p>" +
                        "<p>Esperamos que esta información le resulte valiosa para identificar áreas clave de mejora y optimizar su estrategia digital. Estamos a su disposición para resolver cualquier duda o para profundizar en los resultados del informe.</p>" +
                        "<p>Atentamente,</p>" +
                        "<p><strong>Equipo Temp Tech</strong></p>" +
                        "</div>",
                usuario.getEmpresa().getNombreEmpresa()
        );



        mailService.sendEmailWithImagesAndPdf( toUser, subject, body, headerImage, footerImage, pdf);

    }

    public Map<String, Object> downloadPdf(InputStream graficaImageStream, InputStream termometroImageStream, String authorizationHeader){
        Map<String, Object> respuesta = new HashMap<>();
        try {
            Usuario usuario = jwtService.getUsuarioFromAuthorizationHeader(authorizationHeader);
            String filename = MensajeServicios.NOMBRE_ARCHIVO_RESULTADOS_PDF.getMensaje() + usuario.getEmpresa().getNombreEmpresa();

            File pdf = generatePdf(graficaImageStream, termometroImageStream, filename);

            try (FileInputStream fileInputStream = new FileInputStream(pdf);
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fileInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }

                byte[] pdfBytes = byteArrayOutputStream.toByteArray();

                String encodedPdf = Base64.getEncoder().encodeToString(pdfBytes);

                respuesta.put("respuesta", encodedPdf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            respuesta.put("error", "Error al generar el PDF: " + e.getMessage());
        }
        return respuesta;
    }

    public File generatePdf(InputStream graficaImageStream, InputStream termometroImageStream, String fileName){
        String footerImage = "src/main/java/com/diagnostico_service/helpers/footer.png";
        File pdfFile = null;
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            pdfFile = new File(tempDir, fileName + ".pdf");

            if (pdfFile.exists()) {
                pdfFile.delete();
            }

            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50, 50, 50, 50);

            pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new ImageFooterEventHandler(document, footerImage));

            if (graficaImageStream != null) {
                ImageData imageGraficaData = ImageDataFactory.create(graficaImageStream.readAllBytes());
                Image imageGrafica = new Image(imageGraficaData);
                imageGrafica.setWidth(480);
                document.add(imageGrafica);
            }

            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

            if (termometroImageStream != null) {
                ImageData imageTermometroData = ImageDataFactory.create(termometroImageStream.readAllBytes());
                Image imageTermometro = new Image(imageTermometroData);
                imageTermometro.setWidth(480);
                imageTermometro.setMarginBottom(100);
                document.add(imageTermometro);
            }

            document.close();
            System.out.println("PDF creado exitosamente en: " + pdfFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdfFile;
    }

    private static class ImageFooterEventHandler implements IEventHandler {
        protected Document doc;
        protected String footerImagePath;

        public ImageFooterEventHandler(Document doc, String footerImagePath) {
            this.doc = doc;
            this.footerImagePath = footerImagePath;
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            Rectangle pageSize = page.getPageSize();
            PdfCanvas pdfCanvas = new PdfCanvas(page);

            ImageData imageData = null;
            try {
                imageData = ImageDataFactory.create(footerImagePath);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Image image = new Image(imageData);

            float scaleFactor = pageSize.getWidth() / image.getImageScaledWidth();
            image.scale(scaleFactor, scaleFactor);

            float x = 0;
            float y = pageSize.getBottom();

            Rectangle rootArea = new Rectangle(pageSize.getLeft(), pageSize.getBottom(), pageSize.getWidth(), pageSize.getHeight());
            Canvas canvas = new Canvas(pdfCanvas, rootArea, true);
            canvas.add(image.setFixedPosition(x, y));
            canvas.close();

            pdfCanvas.release();
        }
    }
}
