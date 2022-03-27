$(document).ready(
    function () {
        Chart.defaults.global.elements.rectangle.backgroundColor = '#f7f1da';
        Chart.defaults.global.elements.rectangle.borderColor = '#355564';
        Chart.defaults.global.elements.rectangle.borderWidth = 1;

        const tool = $('#tool').text();
        const reference = $('#reference').text();

        $.get("ajax/categories", {tool: tool, reference: reference},
            function (categories) {
                new Chart($("#categories-chart"), {
                    type: 'horizontalBar',
                    label: 'Categories',
                    data: categories,
                    options: {
                        legend: {
                            display: false
                        }
                    }
                });
            });
        $.get("ajax/types", {tool: tool, reference: reference},
            function (types) {
                new Chart($("#types-chart"), {
                    type: 'horizontalBar',
                    label: 'Types',
                    data: types,
                    options: {
                        legend: {
                            display: false
                        }
                    }
                });
            });

        const detailsTabs = $('#tab-details');
        detailsTabs.find('li:first-child a').tab('show');
    });

