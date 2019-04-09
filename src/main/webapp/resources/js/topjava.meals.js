$(function () {
    makeEditable({
            ajaxUrl: baseAjaxUrl(),
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
                        "des"
                    ]
                ]
            })
        }
    );
});

function baseAjaxUrl() {
    return "ajax/meals/";
}

function filter() {
    context.ajaxUrl = baseAjaxUrl()+"filter?"+$('#filterForm').serialize();
    updateTable();
}

