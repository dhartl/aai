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
		double intensityReference = getHeatMapIntensityReference(distances, request.getIntensityReference());
		logger.info("normalizing distances to max {}", intensityReference);

		return distances.stream().filter(distance -> distance.getCount() > 0)
				.map(distance -> mapToDto(distance, intensityReference)).collect(Collectors.toList());
	}

	private double getHeatMapIntensityReference(List<DistanceBean> distances, Integer intensityReference) {
		long maxDistanceCount = Math.max(distances.stream().mapToLong(DistanceBean::getCount).max().orElse(0), 1);
		if (intensityReference != null && intensityReference > 0) {
			return Math.min(intensityReference, maxDistanceCount);
		} else {
			return maxDistanceCount;
		}
	}

	private HeatMapItemDTO mapToDto(DistanceBean distanceBean, double intensityReference) {
		return new HeatMapItemDTO(distanceBean.getGeoLat(), distanceBean.getGeoLon(),
				BigDecimal.valueOf(Math.min((distanceBean.getCount()) / intensityReference, 1)));
	}

}
