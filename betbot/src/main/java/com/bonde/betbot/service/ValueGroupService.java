package com.bonde.betbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonde.betbot.model.domain.ForecastValue;
import com.bonde.betbot.model.domain.ForecastValueGroup;
import com.bonde.betbot.model.domain.ValueGroup;
import com.bonde.betbot.model.domain.ValueGroupType;
import com.bonde.betbot.repository.ForecastValueGroupRepository;
import com.bonde.betbot.repository.ForecastValueRepository;
import com.bonde.betbot.repository.ValueGroupRepository;

@Service
public class ValueGroupService {

	@Autowired
	private ValueGroupRepository valueGroupRepository;
	
	@Autowired
	private ForecastValueRepository forecastValueRepository;

	@Autowired
	private ForecastValueGroupRepository forecastValueGroupRepository;

	
	public void fillValueGroup()
	{
		

		for(double i = -1 ; i < 1 ; i = i + 0.1)
		{
			String threshold = "from " + (i+0.01) + " to " + (i+0.1);
	
			ValueGroup vg = new ValueGroup();
			vg.setValueGroupType(new ValueGroupType(ValueGroupType.T10));
			vg.setDescription(threshold);
			vg.setMinimumValue(i+0.01);
			vg.setMaximumValue(i+0.1);
			valueGroupRepository.save(vg);
		}

		for(double i = -1 ; i < 1 ; i = i + 0.05)
		{
			String threshold = "from " + (i+0.01) + " to " + (i+0.05);
	
			ValueGroup vg = new ValueGroup();
			vg.setValueGroupType(new ValueGroupType(ValueGroupType.T5));
			vg.setDescription(threshold);
			vg.setMinimumValue(i+0.01);
			vg.setMaximumValue(i+0.05);
			valueGroupRepository.save(vg);
		}

		for(double i = -1 ; i < 1 ; i = i + 0.02)
		{
			String threshold = "from " + (i+0.01) + " to " + (i+0.02);
	
			ValueGroup vg = new ValueGroup();
			vg.setValueGroupType(new ValueGroupType(ValueGroupType.T2));
			vg.setDescription(threshold);
			vg.setMinimumValue(i+0.01);
			vg.setMaximumValue(i+0.02);
			valueGroupRepository.save(vg);
		}

		List<ForecastValue> listFv = forecastValueRepository.findAllOrderByValue();

		for(ForecastValue fv : listFv)
		{
			List<ValueGroup> listVg = valueGroupRepository.findValueGroupByValueInRange(fv.getValue());
			for(ValueGroup vg : listVg)
			{
				ForecastValueGroup fvg = new ForecastValueGroup();
				fvg.setForecastValue(fv);
				fvg.setValueGroup(vg);

				forecastValueGroupRepository.save(fvg);
			}
		}
		
		
		
		
	}

	public static void main(String[] args) {
		ValueGroupService me = new ValueGroupService();
		me.fillValueGroup();
	}
	
}
