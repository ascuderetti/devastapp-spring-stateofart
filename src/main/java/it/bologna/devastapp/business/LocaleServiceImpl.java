package it.bologna.devastapp.business;

import it.bologna.devastapp.business.mapper.LocaleMapper;
import it.bologna.devastapp.business.validator.LocaleValidator;
import it.bologna.devastapp.persistence.LocaleRepository;
import it.bologna.devastapp.persistence.entity.Locale;
import it.bologna.devastapp.presentation.pmo.LocalePmo;
import it.bologna.devastapp.util.MessaggiBusinessSignalUtils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LocaleServiceImpl implements LocaleService {

	@Autowired
	LocaleMapper localeMapper;
	@Autowired
	LocaleValidator localeValidator;
	@Autowired
	LocaleRepository localeRepository;

	@Override
	public List<LocalePmo> findByGestore(Long idGestore) {

		// Validazione Campi Locale
		// localeValidator.validaFindLocaleByGestore(idGestore);

		List<Locale> localiList = localeRepository.findByIdGestore(idGestore);

		List<LocalePmo> localePmoList = localeMapper
				.listLocaleToListLocalePmo(localiList);

		return localePmoList;
	}

	@Override
	@Transactional(readOnly = false)
	public LocalePmo createLocale(LocalePmo localePmo) {

		// Validazione Campi Locale
		localeValidator.validaInserimentoLocale(localePmo);

		if (!MessaggiBusinessSignalUtils.isValidPmo(localePmo)) {
			return localePmo;
		}

		// Mapping da PMO a Entity
		Locale locale = localeMapper.localePmoToLocale(localePmo);

		// Inserimento su DB fisico
		locale = localeRepository.save(locale);

		// Mapping da Entitu a PMO
		localePmo = localeMapper.localeToLocalePmo(locale);

		return localePmo;
	}

	@Override
	@Transactional(readOnly = false)
	public LocalePmo updateLocale(LocalePmo localePmo) {

		// Validazione Campi Locale
		localeValidator.validaAggiornaLocale(localePmo);

		if (!MessaggiBusinessSignalUtils.isValidPmo(localePmo)) {
			return localePmo;
		}

		// Mapping da PMO a Entity
		Locale locale = localeMapper.localePmoToLocale(localePmo);

		// Inserimento su DB fisico
		locale = localeRepository.save(locale);

		// Mapping da Entitu a PMO
		localePmo = localeMapper.localeToLocalePmo(locale);

		return localePmo;
	}

}
