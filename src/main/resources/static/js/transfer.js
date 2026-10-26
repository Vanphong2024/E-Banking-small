document.addEventListener("DOMContentLoaded", function () {
// Lấy các phần tử input từ DOM
const accountInput = document.getElementById("receiverAccountNumber");
// Lấy các phần tử input từ DOM
const receiverName = document.getElementById("receiverName");
//bắt sự kiện khi người dùng nhập số tài khoản
accountInput.addEventListener("input", function() {

    const accountNumber = accountInput.value.trim();
// bắt đầu kiểm tra 
    if (accountNumber.length === 10) {
    // Gửi yêu cầu AJAX để lấy tên người nhận từ server
    fetch(`/bank-account/receiver-name?accountNumber=${accountNumber}`)
        .then(response => response.json())
        .then(data => {
            // Cập nhật tên người nhận vào input receiverName
            receiverName.value = data.receiverName;
        })
        .catch(error => {
            console.error("Lỗi khi lấy tên người nhận:", error);
            receiverName.value = ""; // Xóa giá trị nếu có lỗi
        });
} else {
    receiverName.value = ""; // Xóa giá trị nếu số tài khoản không hợp lệ
}
    console.log(accountNumber);

});
});