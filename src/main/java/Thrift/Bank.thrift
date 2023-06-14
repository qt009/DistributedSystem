namespace java Thrift.src

service BankService {
    LoanResponse requestLoan(1: LoanRequest request)
}
struct LoanRequest {1: double amount}
enum LoanResponse {
    APPROVED,
    REJECTED,
}
