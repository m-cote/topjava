// $(document).ready(function () {
$(function () {
    makeEditable({
            ajaxUrl: "ajax/admin/users/",
            filterUrl: "",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
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
                        "asc"
                    ]
                ]
            })
        }
    );
});

function userEnabledOnChange(userId) {
    let element = document.getElementById("enabled_"+userId);
    setUserEnabled(userId, element.checked);
}

function setUserEnabled(userId, enabled) {

    $.ajax({
        url: context.ajaxUrl + userId+"?enabled="+enabled,
        type: "PATCH"
    }).done(function () {
        successNoty("Updated");
    });
}
