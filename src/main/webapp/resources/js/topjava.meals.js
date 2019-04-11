$(function () {
    makeEditable({
            ajaxUrl: "ajax/meals/",
            filterUrl: "",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function filter() {
    context.filterUrl = "filter?"+$('#filterForm').serialize();
    updateTable();
}

function cancelFilter() {
    context.filterUrl = "";
    $("#filterForm").find(":input").val("");
    updateTable();
}

