package at.c02.aai.app.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import at.c02.aai.app.service.HeatMapService;
import at.c02.aai.app.web.api.out.HeatMapItemDTO;
import at.c02.aai.app.web.api.out.HeatMapRequestDTO;

@Controller
@RequestMapping("/heatmap")
public class HeatMapController {

	@Autowired
	private HeatMapService heatMapService;

	@PostMapping
	@ResponseBody
	public List<HeatMapItemDTO> getHeatMapData(@RequestBody HeatMapRequestDTO request) {
		return heatMapService.getHeatMapData(request);
	}
}
