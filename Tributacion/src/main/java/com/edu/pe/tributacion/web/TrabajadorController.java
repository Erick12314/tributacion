package com.edu.pe.tributacion.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.edu.pe.tributacion.model.Trabajador;
import com.edu.pe.tributacion.repository.TrabajadorRepository;

@Controller
public class TrabajadorController {

	@Autowired
	private TrabajadorRepository trabajadorRepository;

	private static final double[] TASAS = { 0.08, 0.14, 0.17, 0.2, 0.3 };
	
	
	private double round(double valor, int q_decimales) {
	    if (q_decimales < 0) return 0;

	    long factor = (long) Math.pow(10, q_decimales);
	    valor = valor * factor;
	    long tmp = Math.round(valor);
	    return (double) tmp / factor;
	}
	
	private Trabajador calcularIR(Trabajador trabajador) {
		
		long uit = trabajador.getUit();
		long[] ESCALA_UIT = { uit * 5, uit * 20, uit * 35, uit * 45 };
		double renta_neta_gravable = 0;
		
		
		double[] impuestos = { 0, 0, 0, 0, 0 };
		double[] monto_afecto = { 0, 0, 0, 0, 0 };
		double monto_afecto_total = 0;
		
		// BRUTO - TAXES = NETO
		double ingreso_bruto_anual = 0;
		double total_impuesto = 0;
		
		
		if (trabajador.getRelacion_laboral().equalsIgnoreCase("Dependiente")) {

			ingreso_bruto_anual = trabajador.getSalario() * 14;
		} else {

			ingreso_bruto_anual = trabajador.getSalario() * 12;
		}
		//BL
		long monto_uit_descontable = uit * 7;

		renta_neta_gravable = ingreso_bruto_anual - monto_uit_descontable;

		if (renta_neta_gravable > 0) {

			if (renta_neta_gravable <= ESCALA_UIT[0]) {

				monto_afecto[0] = renta_neta_gravable;
				impuestos[0] = monto_afecto[0] * TASAS[0];

			} else if (renta_neta_gravable <= ESCALA_UIT[1]) {

				monto_afecto[0] = ESCALA_UIT[0];
				impuestos[0] = monto_afecto[0] * TASAS[0];

				monto_afecto[1] = renta_neta_gravable - monto_afecto[0];
				impuestos[1] = monto_afecto[1] * TASAS[1];

			} else if (renta_neta_gravable <= ESCALA_UIT[2]) {

				monto_afecto[0] = ESCALA_UIT[0];
				impuestos[0] = monto_afecto[0] * TASAS[0];

				monto_afecto[1] = ESCALA_UIT[1] - monto_afecto[0];
				impuestos[1] = monto_afecto[1] * TASAS[1];

				monto_afecto[2] = renta_neta_gravable - (monto_afecto[0] + monto_afecto[1]);
				impuestos[2] = monto_afecto[2] * TASAS[2];

			} else if (renta_neta_gravable <= ESCALA_UIT[3]) {
				
				monto_afecto[0] = ESCALA_UIT[0];
				impuestos[0] = monto_afecto[0] * TASAS[0];

				monto_afecto[1] = ESCALA_UIT[1] - monto_afecto[0];
				impuestos[1] = monto_afecto[1] * TASAS[1];

				monto_afecto[2] = ESCALA_UIT[2] - (monto_afecto[0] + monto_afecto[1]);
				impuestos[2] = monto_afecto[2] * TASAS[2];
				
				monto_afecto[3] = renta_neta_gravable - (monto_afecto[0] + monto_afecto[1] + monto_afecto[2]);
				impuestos[3] = monto_afecto[3] * TASAS[3];
				
			} else {
				
				monto_afecto[0] = ESCALA_UIT[0];
				impuestos[0] = monto_afecto[0] * TASAS[0];

				monto_afecto[1] = ESCALA_UIT[1] - monto_afecto[0];
				impuestos[1] = monto_afecto[1] * TASAS[1];

				monto_afecto[2] = ESCALA_UIT[2] - (monto_afecto[0] + monto_afecto[1]);
				impuestos[2] = monto_afecto[2] * TASAS[2];
				
				monto_afecto[3] = ESCALA_UIT[3] - (monto_afecto[0] + monto_afecto[1] + monto_afecto[2]);
				impuestos[3] = monto_afecto[3] * TASAS[3];
				
				monto_afecto[4] = renta_neta_gravable - (monto_afecto[0] + monto_afecto[1] + monto_afecto[2] + monto_afecto[3]);
				impuestos[4] = monto_afecto[4] * TASAS[4];
			}
			
			for (int i = 0; i < impuestos.length; i++) {
				total_impuesto += impuestos[i];
				monto_afecto_total += monto_afecto[i];
				
				System.out.println("monto ["+ i + "] -> " + monto_afecto[i] + "|| impuesto ["+ i + "] -> " +impuestos[i]);
			}
			
			trabajador.setTotal_bruto(round(ingreso_bruto_anual, 2));
			trabajador.setTotal_impuesto(round(total_impuesto, 2));
			trabajador.setTotal_neto(round(ingreso_bruto_anual - total_impuesto, 2));
			
			System.out.println("suma de los montos " + monto_afecto_total);
			
		}else {
			trabajador.setTotal_bruto(ingreso_bruto_anual);
			trabajador.setTotal_impuesto(0);
			trabajador.setTotal_neto(ingreso_bruto_anual);
		}
		
		return trabajador;
	}
	
	
	@GetMapping("/trabajador/calcularIR")
	private String formulario(Model model) {

		model.addAttribute(new Trabajador());
		return "formulario";
	}

	@PostMapping("/trabajador/calcularIR")
	private String calculado(@Valid Trabajador trabajador, BindingResult bindingResult) {
		
		if (bindingResult.hasFieldErrors()) {
			return "formulario";
		}
		
		trabajador = calcularIR(trabajador);
		
		return "calculoIR";
	}

	@PostMapping("/trabajador/new")
	private String guardarTrabajador(@ModelAttribute Trabajador trabajador) {

		trabajadorRepository.save(trabajador);
		
		return "redirect:/trabajador/lista";
	}

	@GetMapping("/trabajador/lista")
	private String listado(Map<String, Object> model) {
		
		List<Trabajador> trabajadores = trabajadorRepository.findAll();
		model.put("trabajadores", trabajadores);
		return "listado";
	}

}
