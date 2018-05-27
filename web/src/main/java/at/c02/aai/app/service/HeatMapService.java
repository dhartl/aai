package at.c02.aai.app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.c02.aai.app.service.bean.DistanceBean;
import at.c02.aai.app.web.api.out.DoctorOutDTO;
import at.c02.aai.app.web.api.out.HeatMapItemDTO;
import at.c02.aai.app.web.api.out.HeatMapRequestDTO;
import at.c02.aai.app.web.api.out.PharmacyOutDTO;

@Service
public class HeatMapService {

	private static final Logger logger = LoggerFactory.getLogger(HeatMapService.class);

	@Autowired
	private PharmacyExportService pharmacyExportService;

	@Autowired
	private DoctorExportService doctorExportService;

	@Autowired
	private DistanceService distanceService;

	public List<HeatMapItemDTO> getHeatMapData(HeatMapRequestDTO request) {
		List<PharmacyOutDTO> pharmacies = pharmacyExportService.findPharmacies();
		List<DoctorOutDTO> doctors = doctorExportService.findDoctors(request.getDoctorRequest());

		List<DistanceBean> distances = distanceService.createDistances(pharmacies, doctors,
				request.getMaxDistanceInMeter());
		double maxCount = distances.stream().mapToLong(DistanceBean::getCount).max().orElse(0);
		logger.info("normalizing distances to max {}", maxCount);

		return distances.stream().map(distance -> mapToDto(distance, maxCount)).collect(Collectors.toList());
	}

	private HeatMapItemDTO mapToDto(DistanceBean distanceBean, double maxCount) {
		return new HeatMapItemDTO(distanceBean.getGeoLat(), distanceBean.getGeoLon(),
				BigDecimal.valueOf((distanceBean.getCount()) / maxCount));
	}

}
