document.addEventListener("DOMContentLoaded", function () {
    const accountInput = document.getElementById("receiverAccountNumber");
    const receiverName = document.getElementById("receiverName");

    accountInput.addEventListener("input", function () {
        const accountNumber = accountInput.value.trim();

        if (accountNumber.length !== 10) {
            receiverName.value = "";
            return;
        }

        receiverName.value = "Đang tra cứu...";

        fetch(`/transactions/receiver-name?accountNumber=${encodeURIComponent(accountNumber)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Tra cứu người nhận thất bại (${response.status})`);
                }
                return response.json();
            })
            .then(data => {
                if (typeof data.receiverName !== "string" || data.receiverName.trim() === "") {
                    throw new Error("Phản hồi không có tên người nhận");
                }
                receiverName.value = data.receiverName;
            })
            .catch(error => {
                console.error("Lỗi khi lấy tên người nhận:", error);
                receiverName.value = error.message.includes("(404)")
                    ? "Không tìm thấy tài khoản"
                    : "Không thể tra cứu người nhận";
            });
    });
});