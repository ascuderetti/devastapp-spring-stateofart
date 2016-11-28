package it.bologna.devastapp.business.mapper;

import it.bologna.devastapp.presentation.pmo.TestEntity;
import it.bologna.devastapp.presentation.pmo.TestPmo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface MapperStruct {

	@Mappings(value = { @Mapping(source = "position", target = "posizione"),
			@Mapping(source = "stuff", target = "listaProdotti") })
	TestPmo offertaToOffertaPmo(TestEntity offertaEntity);

}
