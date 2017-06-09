package com.bonde.betbot.service;

import java.util.List;

import javax.transaction.Transactional;

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

	@Transactional
	public void fillValueGroup()
	{
		

		for(double i = -100 ; i < 100 ; i = i + 10)
		{
			String threshold = "from " + (i+1) + " to " + (i+10);
	
			ValueGroup vg = new ValueGroup();
			vg.setValueGroupType(new ValueGroupType(ValueGroupType.T10));
			vg.setDescription(threshold);
			vg.setMinimumValue((i+1)/100);
			vg.setMaximumValue((i+10)/100);
			valueGroupRepository.save(vg);
		}

		for(double i = -100 ; i < 100 ; i = i + 5)
		{
			String threshold = "from " + (i+1) + " to " + (i+5);
	
			ValueGroup vg = new ValueGroup();
			vg.setValueGroupType(new ValueGroupType(ValueGroupType.T5));
			vg.setDescription(threshold);
			vg.setMinimumValue((i+1)/100);
			vg.setMaximumValue((i+5)/100);
			valueGroupRepository.save(vg);
		}

		for(double i = -100 ; i < 100 ; i = i + 2)
		{
			String threshold = "from " + (i+1) + " to " + (i+2);
	
			ValueGroup vg = new ValueGroup();
			vg.setValueGroupType(new ValueGroupType(ValueGroupType.T2));
			vg.setDescription(threshold);
			vg.setMinimumValue((i+1)/100);
			vg.setMaximumValue((i+2)/100);
			valueGroupRepository.save(vg);
		}

		for(double i = -1 ; i <= 1 ; i = i + 0.01)
		{
			ForecastValue fv = new ForecastValue();
			fv.setValue(i);
			forecastValueRepository.save(fv);
		}
		
		List<ForecastValue> listFv = forecastValueRepository.findAllByOrderByValueAsc();

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
