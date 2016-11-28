var DateUtils = {

	addUnOra : function(data) {
		return data.addHours(1);
	},

	cambiaDataInizioOfferta : function(offerta) {

		offerta.dataFine = offerta.dataInizio.clone();
		offerta.dataFine = DateUtils.addUnOra(offerta.dataFine);
	}
};

