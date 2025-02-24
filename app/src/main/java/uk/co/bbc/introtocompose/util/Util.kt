package uk.co.bbc.introtocompose.util

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100 else 0.0

}

fun calculateTotalPerPerson(
    totalBill: Double,
    splitBillBy: Int,
    tipPercentage: Int
): Double {
    val bill = calculateTotalTip(totalBill, tipPercentage) + totalBill;
    return bill / splitBillBy
}