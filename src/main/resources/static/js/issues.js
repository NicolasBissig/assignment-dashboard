$(document).ready(
    function () {
        var table = $('#issues').DataTable({
            "ajax": 'ajax/issues'
        });
        $('#issues tbody').on('click', 'tr', function () {
            var data = table.row( this ).data();
            window.open("details?tool="+ data[0] +"&reference="+ data[2] , "Details Report");
        } );
    }
);

