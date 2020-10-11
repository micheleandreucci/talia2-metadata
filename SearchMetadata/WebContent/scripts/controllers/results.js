'use strict';

/**
 * @ngdoc function
 * @name deliverySearchApp.controller:ResultsCtrl
 * @description
 * # ResultsCtrl
 * Controller of the deliverySearchApp
 */
angular.module('deliverySearchApp')
  .controller('ResultsCtrl', function () {
    /*this.results = [
      {
        fileName: "2.2.2-Catalonia_Tematic_factsheet.pdf",
        summary: "The first Spanish coworking space was opened in 2007 in Catalonia, in Barcelona. However, already in 1995, a space aimed at providing a coworking space, without being called coworking space, opened in Barcelona. In fact, it was not until 2008 when coworking activities started to spread across Europe. The survey results show that a 17.7% of coworking spaces in Catalonia began their activities before 2012. Specifically, 10 out of 40 of the Catalan spaces were opened before that date.",
        delDate:"21-09-2018",
        delName:"2.2.2 THEMATIC FACTSHEETS",
        delDescription:"Country overview and summary of the local and regional workshops...",
        delKeywords:"clustering, economic cooperation, ...",
        delTargets:"Local public authority, ...",
        projUrl:"https://coworkmed.interreg-med.eu/",
        projName:"COWORKMed"
      },
      {
        fileName: "2.2.2-Catalonia_Tematic_factsheet.pdf",
        summary: "The first Spanish coworking space was opened in 2007 in Catalonia, in Barcelona. However, already in 1995, a space aimed at providing a coworking space, without being called coworking space, opened in Barcelona. In fact, it was not until 2008 when coworking activities started to spread across Europe. The survey results show that a 17.7% of coworking spaces in Catalonia began their activities before 2012. Specifically, 10 out of 40 of the Catalan spaces were opened before that date.",
        delDate:"21-09-2018",
        delName:"2.2.2 THEMATIC FACTSHEETS",
        delDescription:"Country overview and summary of the local and regional workshops...",
        delKeywords:"clustering, economic cooperation, ...",
        delTargets:"Local public authority, ...",
        projUrl:"https://coworkmed.interreg-med.eu/",
        projName:"COWORKMed"
      },
    ];*/

    this.results = [];
    this.pager = {};
    this.setPage = setPage;

    initController();

    function initController() {
        // initialize to page 1
        this.setPage(1);
    }

    function setPage(page) {
        if (page < 1 || page > vm.pager.totalPages) {
            return;
        }

        // get pager object from service
        
        
        this.pager = PagerService.GetPager(this.results.length, page);

        // get current page of items
        this.resultsDisp = vm.dummyItems.slice(this.resultsDisp.startIndex, this.results.endIndex + 1);
    }
  });
