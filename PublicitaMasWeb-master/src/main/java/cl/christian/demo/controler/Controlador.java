package cl.christian.demo.controler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.jar.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.christian.demo.interfaceService.ICampaniaPublicitariaService;
import cl.christian.demo.interfaceService.ICartelPublicitarioService;
import cl.christian.demo.modelo.CampaniaPublicitaria;
import cl.christian.demo.modelo.CartelPublicitario;

/**
 * @author Christian
 *
 */

@Controller
@RequestMapping
public class Controlador {
	
	@Autowired
	private ICampaniaPublicitariaService service;
	
	/**
	 * carga los datos de la bd en una lista y luego los manda 
	 * a una vista 
	 */
	@GetMapping("/listar")
	public String listarcampania(Model model) {
		List<CampaniaPublicitaria>campaniapublicitaria=service.listarcampania();
		model.addAttribute("campaniapublicitaria", campaniapublicitaria);
		return "index";
	}
	
	/**
	 * carga los datos de la bd en una lista y luego los manda 
	 * a una vista 
	 */
	@GetMapping("/listarPublicidad")
	public String listarcampaniaPublidad(Model model) {
		List<CampaniaPublicitaria>campaniapublicitaria=service.listarcampania();
		model.addAttribute("campaniapublicitaria", campaniapublicitaria);
		return "listapublicidadgeneral";
	}

	
	
	
	/**
	 * muestra la vista formulario agregar campaña
	 * cuando lo llamn por "/new"
	 */
	@GetMapping("/new")
	public String agregarcampania(Model model) {
		model.addAttribute("campaniapublicitaria", new CampaniaPublicitaria());
		return "usuario/FormularioAgregarCampaña";
	}
	
	
	
	/**
	 * @param c
	 * @param model
	 *Guarda los datos del formulario campaña  y la imagen
	 * los guarda en la base de dato
	 * @param attributes
	 * @return
	 */
	@PostMapping("/savecampania")
	public String savecampania(@Validated CampaniaPublicitaria c, Model model, @RequestParam("file") MultipartFile image, RedirectAttributes attributes, RedirectAttributes redirectAttrs) {
		
		
		if(!image.isEmpty()) {
			
			String rutaAbsoluta = "C://Campania//recursos";
			
			try {
				byte[] bytesImg =image.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//"+ image.getOriginalFilename());
				Files.write(rutaCompleta, bytesImg);
				
				c.setImage(image.getOriginalFilename());
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		

		
		model.addAttribute("titulo","Agregar  Campania-Empresa");
		service.savecampania(c);
		redirectAttrs
        .addFlashAttribute("mensaje", "Agregado correctamente")
        .addFlashAttribute("clase", "success");
		return "redirect:/listar";	
	}
	
	/**
	 * primero busca en la lista el id que le mandamos atraves de un boton
	 * y luego manda los datos para cargalos
	 * para luego poder modificarlo llamando a la clase save de campaña
	 */
	@GetMapping("modificarcampania/{id}")
	public String editar(@PathVariable("id") int id,Model model, RedirectAttributes redirectAttrs) {
		
		CampaniaPublicitaria campaniapublicitaria=service.listarId(id);
		
		model.addAttribute("titulo","Modificar Campania-Empresa");
		model.addAttribute("campaniapublicitaria",campaniapublicitaria);
		
		redirectAttrs
        .addFlashAttribute("mensaje", "Modificado correctamente")
        .addFlashAttribute("clase", "success");
		
		return"usuario/FormularioAgregarCampaña";
	}
	
	
	/*Detalle de campaña por id*/
	
	/**
	 * Buscar el id en la lista
	 * Buscar si existe o no 
	 * Luego de encontrarlo lo manda a una vista que detalla la campaña y sus datos
	 * 
	 */
	@GetMapping("detalle/{id}")
	public String detalleCampania(@PathVariable("id") int id,Model model,RedirectAttributes attribute) {
		
		 CampaniaPublicitaria campaniapublicitaria= null;
		
		
		  if(id > 0) {
			  campaniapublicitaria = service.listarId(id);
		  
		 if(campaniapublicitaria==null) {
			 attribute.addFlashAttribute("error","El ID del la Campania no existe");
			 return "DetalleCampania";
		 }
		  }else {
			  attribute.addFlashAttribute("error","error con el id de la campania");
			  return "DetalleCampania";
		  }
		model.addAttribute("titulo","Detalle de  "+ campaniapublicitaria.getNombre());
		  
		model.addAttribute("campaniapublicitaria", campaniapublicitaria);
		return"DetalleCampania";
	}
	
	
	
	@GetMapping("/nombre/detalle/{id}")
	public String detalleCampaniaNombre(@PathVariable("id") int id,Model model,RedirectAttributes attribute) {
		
		 CampaniaPublicitaria campaniapublicitaria= null;
		
		
		  if(id > 0) {
			  campaniapublicitaria = service.listarId(id);
		  
		 if(campaniapublicitaria==null) {
			 attribute.addFlashAttribute("error","El ID del la Campania no existe");
			 return "DetalleCampania";
		 }
		  }else {
			  attribute.addFlashAttribute("error","error con el id de la campania");
			  return "DetalleCampania";
		  }
		model.addAttribute("titulo","Detalle de  "+ campaniapublicitaria.getNombre());
		  
		model.addAttribute("campaniapublicitaria", campaniapublicitaria);
		return"DetalleCampania";
	}
	
	
	
	
	

	@GetMapping("/listausuario")
	public String listaCampaniasUsuarios(Model model) {

	
		model.addAttribute("campaniapublicitaria", new CampaniaPublicitaria());
		return "usuario/ListaDeCampaniaUsuario";
		
	}
	
	@GetMapping("/listanombre")
	public String listaCampaniasNombre(Model model) {

	
		model.addAttribute("campaniapublicitaria", new CampaniaPublicitaria());
		return "ListaDeCampaniaNombre";
		
	}
	
	@GetMapping("/nombre/")
	public String BuscarPorNombre(@RequestParam String nombre,Model model,@ModelAttribute("campaniapublicitaria") CampaniaPublicitaria campaniaPublicitaria) {
		
		model.addAttribute("PublicidadPorNombre", service.BuscarPornombre(nombre));
		
		
		return "ListaDeCampaniaNombre";
	}
	
	/*
	 * Este metodo busca por el id del usuario todas las campañas que tenga
	 * vinculadas o que allá creado
	 */
	/**
	 * Buscar en toda la lista el idusuario que sea igual al indicado
	 * para luego agregarlos a una lista, para mostrarlos en una vista
	 */
	@GetMapping("/idusuario/")
	public String BuscarPorIdusuario(@RequestParam String idusuario,Model model,@ModelAttribute("campaniapublicitaria") CampaniaPublicitaria campaniaPublicitaria ) {

		model.addAttribute("PublicidadPorUsuario", service.BuscarPordusuario(idusuario));
		return "usuario/ListaDeCampaniaUsuario";
	}
	
	
	@GetMapping("/inicioAdmin")
	//llama
	public String inicioAdmin(Model model) {
		
		return "inicioAdmin";
	}
	
	
	
		
	/**
	 * Busca el id en la lista para luego para luego tomar el objeto e
	 * elimina una campaña por su id 
	 */
		@GetMapping("/eliminarcampania/{id}")
	public String delete(Model model, @PathVariable int id) {
		service.delete(id);
		return "redirect:/listar";
	}
	
		
	/*Desde Esta parte comienzan las clases de los carteles publicitarios*/
	@Autowired
	private ICartelPublicitarioService servicecartel;
	
	/**
	 * Toma los datos de la bd 
	 * y crea una lista  de tipo CartelPublicitario para luego  mostrarlos en una vista
	 * llamada listacartelesAdmin
	 */
	
	@GetMapping("/listacartelesAdmin")
	public String listarcartelesadmin(Model model) {
		List<CartelPublicitario> cartelpublicitarios = servicecartel.listarcarteles();
		model.addAttribute("cartelpublicitarios", cartelpublicitarios);
		return "carteles/listacartelesAdmin";
	} 
	/**
	 * Toma los datos de la bd 
	 * y crea una lista  de tipo CartelPublicitario para luego  mostrarlos en una vista
	 * llamada listacarteles
	 */
	@GetMapping("/listacarteles")
	public String listarcarteles(Model model) {
		List<CartelPublicitario> cartelpublicitarios = servicecartel.listarcarteles();
		model.addAttribute("cartelpublicitarios", cartelpublicitarios);
		return "carteles/listacarteles";
	} 
	
	/**
	 * llama a la vista FormularioAgregarCartel.html
	 * y la muestra.
	 * ademas le entrega a la vista un texto"Agregar Cartel Publicitario" y un tipo Ojebto CartelPublicitario para luego Usarlo
	 */
	@GetMapping("/nuevocartel")
	public String nuevocartel(Model model) {
		model.addAttribute("titulo","Agregar Cartel Publicitario");
		model.addAttribute("cartelpublicitario", new CartelPublicitario());
		return "carteles/FormularioAgregarCartel";
	}
	
	/**
	 * Esta Clase guarda el formulario de cartel
	 * Ademas guarda las imagenes
	 *
	 * @return
	 */
	@PostMapping("/savecartel")
	public String savecartel(@Validated CartelPublicitario c, Model model, @RequestParam("file") MultipartFile image, @RequestParam("document") MultipartFile documentos,RedirectAttributes atrAttributes,RedirectAttributes redirectAttrs) {
		
		if(!image.isEmpty() && ! documentos.isEmpty()) {
			
			String rutaAbsoluta = "C://Campania//recursos";
			
			String rutaAbsolutaDoc = "C://Campania//recursos";
			
			try {
				byte[] bytesImg = image.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + image.getOriginalFilename());
				byte[] bytesDoc = documentos.getBytes();
				Path rutaCompletaDoc = Paths.get(rutaAbsolutaDoc + "//" + documentos.getOriginalFilename());
			
				Files.write(rutaCompleta, bytesImg);
				Files.write(rutaCompletaDoc, bytesDoc);
				
				c.setImage(image.getOriginalFilename());
				c.setDocumentacion(documentos.getOriginalFilename());
				
					
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	    redirectAttrs
        .addFlashAttribute("mensaje", "Agregado correctamente")
        .addFlashAttribute("clase", "success");
		
		servicecartel.savecartel(c);
		return "redirect:/listacartelesAdmin";
	}
	
	/**
	 * Buscar en la lista de carteles por el id entregado
	 * luego entrega el id con el objeto asociado
	 * y manda los datos a una vista para cargarlos 
	 */
	@GetMapping("modificarcartel/{id}")
	public String editarcartel(@PathVariable int id,Model model, RedirectAttributes redirectAttrs) {
		CartelPublicitario cartelpublicitario=servicecartel.listarIdcartel(id);
		
		model.addAttribute("titulo","Modificar-Cartel  "+ cartelpublicitario.getId());
		model.addAttribute("cartelpublicitario",cartelpublicitario);
		
		
	    redirectAttrs
        .addFlashAttribute("mensaje", "Modificado correctamente")
        .addFlashAttribute("clase", "success");
		
		return"carteles/FormularioAgregarCartel";
	}
	/*lista por idusuario de carteles ingresados*/
	@PostMapping("/listacarteles_usuario/{idusuario}")
	public String listarcarteles_usuarios(@PathVariable int idusuario,Model model) {
		
		 
		
		model.addAttribute("cartelpublicitarios", servicecartel.listarIdcartelUsuario(idusuario));

		return "listacarteles_usuario";
	}
	
	
	/**
	 * Buscar en la lista de carteles el id entregado y luego de encontralo elimina todo el objeto o la fila de la bd
	 */
	@GetMapping("/eliminarcartel/{id}")
	public String deletecartel(Model del,@PathVariable int id) {
		servicecartel.deletecartel(id);
		return"redirect:/listacartelesAdmin";
	}
	@GetMapping("/agregarCartel")
	//llama
	public String agregarcartel(Model model) {
		model.addAttribute("cartelpublicitario", new CartelPublicitario());
		return "carteles/FormularioAgregarCartel";
	}
	
	/**
	 * iniciamos un objeto en null
	 * luego preguntamos el id entregado es mayor a 0 si es asi 
	 * buscamos en la lista de carteles el id y si lo encuentra por carga en el objeto vacio iniciado anteriormente
	 * 
	 */
	@GetMapping("detallecartel/{id}")
	public String detalleCartel(@PathVariable("id") int id,Model model,RedirectAttributes attribute) {
		
		 CartelPublicitario cartelPublicitario = null;
		
		
		  if(id > 0) {
			  cartelPublicitario = servicecartel.listarIdcartel(id);
		  
		 if(cartelPublicitario==null) {
			 attribute.addFlashAttribute("error","El ID del la Cartel no existe");
			 return "DetalleCampania";
		 }
		  }else {
			  attribute.addFlashAttribute("error","error con el id del cartel");
			  return "DetalleCampania";
		  }
		model.addAttribute("titulo","Detalle de  "+ cartelPublicitario.getTitulo());
		  
		model.addAttribute("cartelpublicitario", cartelPublicitario);
		return"carteles/DetalleCartel";
	}
	
	@GetMapping("/index")
	//llama de inicio 
	public String incio(Model model) {
		
		return "inicio";
	}
	
	@GetMapping("/inicioUsuario")
	//llama de inicio 
	public String incioUsuario(Model model) {
		
		return "inicioUsuario";
	}
}