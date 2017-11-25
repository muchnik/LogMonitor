var order = -1;

function sortTable(col) {
    var table = document.getElementById("main_table");
    var tb = table.tBodies[0], tr = Array.prototype.slice.call(tb.rows, 0), i;
    var reverse = order;
    reverse = -((+reverse) || -1);
    order *= -1;
    tr = tr.sort(function (a, b) {
        a = a.cells[col].textContent.trim();
        b = b.cells[col].textContent.trim();
        return reverse * (Number(a.match(/(\d+)/g)[0]) - Number((b.match(/(\d+)/g)[0])));
    });
    for (i = 0; i < tr.length; ++i) tb.appendChild(tr[i]);
}