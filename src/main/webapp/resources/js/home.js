function aggiungiOffertaInMappa(offerta, gestore, map) {

	var idLocale = offerta.idLocale;
	var locali = gestore.listaLocali;
	//recupero il locale dall'id
	var locale = locali[_.findIndex(locali, {
		'id' : idLocale
	})];

	//recupero le coordinate geo per inserire il marker nella mappa
	var lat = locale.posizione.latitudine;
	var lon = locale.posizione.longitudine;

	var marker = L.marker([lat, lon]).addTo(map);
	marker.bindPopup("<b>Offerta</b><br> Attiva da " + offerta.dataInizio.toString("d-MMM-yyyy HH-mm") + " a " + offerta.dataFine.toString("d-MMM-yyyy HH-mm") + ".").openPopup();
	var circle = L.circle([lat, lon], 500, {
		color : 'red',
		fillColor : '#f03',
		fillOpacity : 0.5
	}).addTo(map);

};

(function() {

	/*
	 * MODEL
	 */

	var pagamento = {
		id : null,
		listaIdOfferta : [],
		idGestore : null,
		titoloAcquisto : "titolo acquisto prova",
		importoTotale : 20,
		dataPagamento : "2014-06-28T19:08:44.000+02:00",
		pagamentoID : "PAY-96N13419KE4300357KOOIDHA",
		redirectURL : "http://test.com",
		accessToken : "Bearer A015FvifT29eMQ0NNuAHUmRyKJAiZpFA8pPcJ3Dm840lqIg",
		stato : "created",
		payerID : "FK2W2LSRMPZZN"
	};

	// var prodotti = {};

	var map = L.map('map').setView([44.496, 11.339], 15);
	L.tileLayer('http://{s}.tiles.mapbox.com/v3/klaodeli.ieppe537/{z}/{x}/{y}.png', {
		attribution : 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://mapbox.com">Mapbox</a>',
		maxZoom : 18
	}).addTo(map);

	var app = angular.module('devastapp', ['ui.bootstrap']);

	app.controller('MainController', function($scope, prodottoRepository, gestoreRepository) {

		// $scope.gestore

		prodottoRepository.getAllProdotti().success(function(data) {
			$scope.prodotti = data.dati.lista;
		});

		gestoreRepository.getAllGestori().success(function(data) {
			$scope.gestori = data.dati.lista;
		});

		this.setGestore = function(gestore) {
			$scope.gestore = gestore;
		};

	});

	app.controller('TabController', function() {
		this.tab = 1;
		this.isSet = function(checkTab) {
			return this.tab === checkTab;
		};
		this.setTab = function(setTab) {
			this.tab = setTab;
		};
	});

	app.controller('OffertaController', function($scope, offertaRepository, pagamentoRepository, notificaRepository) {

		$scope.offerta = {};
		$scope.offerta.dataInizio = Date.today();
		$scope.offerta.dataFine = $scope.offerta.dataInizio.clone();
		$scope.offerta.dataFine = DateUtils.addUnOra($scope.offerta.dataFine);

		$scope.newOfferta = function(gestore) {

			offertaRepository.inserisciOfferta(this.offerta).success(function(data, status, headers, config) {
//				alert("SUCCESS" + JSON.stringify(data, null, '\t'));
				$scope.offerta.id = data.dati.id;

				/*
				 * Paga Offerta
				 */
//				pagamento.listaIdOfferta.push($scope.offerta.id);
//				pagamento.idGestore = gestore.id;
//				pagamentoRepository.pagaOfferte(pagamento).success(function(data, status, headers, config) {

//					alert("SUCCESS" + JSON.stringify(data, null, '\t'));

					var idLocale = $scope.offerta.idLocale;
					var locali = gestore.listaLocali;
					// recupero  il locale  dall'id
					var locale = locali[_.findIndex(locali, {
						'id' : idLocale
					})];

					// recupero le coordinate geo per inserire il marker  nella mappa
					var lat = locale.posizione.latitudine;
					var lon = locale.posizione.longitudine;

					var marker = L.marker([lat, lon]).addTo(map);
					marker.bindPopup("<b>Offerta</b><br> Attiva da " + $scope.offerta.dataInizio.toString("d-MMM-yyyy HH-mm") + " a " + $scope.offerta.dataFine.toString("d-MMM-yyyy HH-mm") + ".").openPopup();
					var circle = L.circle([lat, lon], 500, {
						color : 'red',
						fillColor : '#f03',
						fillOpacity : 0.5
					}).addTo(map);

					/*
					 * Chiamo il metodo di invia notifica
					 */
					notifica = {};
					notifica.testo = "offerta inserita";

					notificaRepository.offertaInserita(notifica).success(function(data, status, headers, config) {
		
//						alert("SUCCESS" + JSON.stringify(data, null, '\t'));
						
						alert("Offerta inserita!");
		
		
					}).error(function(data, status, headers, config) {
						// called asynchronously if an error occurs
						// or server returns response with an error status.
						alert("ERROR" + JSON.stringify(data, null, '\t'));
					});

					/*
					 * Reset Offerta
					 */
					$scope.offerta.id = null;

//				}).error(function(data, status, headers, config) {
//					// called asynchronously if an error occurs
//					// or server returns response with an error status.
//					alert("ERROR" + JSON.stringify(data, null, '\t'));
//				}
				
//				);

			}).error(function(data, status, headers, config) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				alert("ERROR" + JSON.stringify(data, null, '\t'));
			});

		};
	});

	app.controller('DatepickerDemoCtrl', function($scope) {
		var today = Date.today();
		$scope.hstep = 1;
		$scope.mstep = 1;
		$scope.minDate = today;
		$scope.format = 'dd-MMMM-yyyy';
		$scope.ismeridian = false;
		// $scope.initDate = new Date();

		$scope.open = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			$scope.opened = true;
		};

		$scope.clicked = function(e) {
			e.preventDefault();
			e.stopPropagation();
		};

		$scope.dateOptions = {
			formatYear : 'yy',
			startingDay : 1
		};

		$scope.changed = function(offerta) {
			offerta.dataFine = offerta.dataInizio.clone();
			offerta.dataFine = DateUtils.addUnOra(offerta.dataFine);
			console.log('Data Fine: ' + offerta.dataFine);
		};
	});

	app.controller('FidelizzazioneController', function($scope, offertaRepository, pagamentoRepository, localeRepository) {

		$scope.fidelizzazione = {};
		$scope.fidelizzazione.dataInizio = Date.today();
		// $scope.offerta.dataFine = DateUtils.addUnOra($scope.offerta.dataFine);

		$scope.newFidelizzazione = function(gestore) {

			var idLocale = $scope.fidelizzazione.idLocale;
			var locali = gestore.listaLocali;
			// recupero  il locale  dall'id
			var locale = locali[_.findIndex(locali, {
				'id' : idLocale
			})];
			locale.fidelizzazione = true;
			
			localeRepository.aggiornaLocale(locale).success(function(data, status, headers, config) {
				
//				alert("SUCCESS" + JSON.stringify(data, null, '\t'));
				alert("Servizio fidelizzazione attivato!");


			}).error(function(data, status, headers, config) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
				alert("ERROR" + JSON.stringify(data, null, '\t'));
			});
			

		};
	});

	app.factory('prodottoRepository', function($http) {
		return {
			getAllProdotti : function() {
				return $http.get(RestUrl.GET_PRODOTTI);
			}
		};
	});

	app.factory('localeRepository', function($http) {
		return {
			aggiornaLocale : function(locale) {
				return $http.put(RestUrl.AGGIORNA_LOCALE,locale);
			}
		};
	});
	
	app.factory('gestoreRepository', function($http) {
		return {
			getAllGestori : function() {
				return $http.get(RestUrl.GET_GESTORI);
			}
		};
	});

	app.factory('offertaRepository', function($http) {
		return {
			inserisciOfferta : function(offertaDaInserire) {
				return $http.post(RestUrl.INSERISCI_OFFERTA, offertaDaInserire);
			}
		};
	});

	app.factory('pagamentoRepository', function($http) {
		return {
			pagaOfferte : function(pagamento) {
				return $http.post(RestUrl.INSERISCI_PAGA, pagamento);
			}
		};
	});

	app.factory('notificaRepository', function($http) {
		return {
			offertaInserita : function(notifica) {
				return $http.post(RestUrl.NOTIFICA_OFFERTA_INSERITA, notifica);
			}
		};
	});

})();
