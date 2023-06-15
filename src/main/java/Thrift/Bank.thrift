namespace java Thrift.src

service BankService {
    void requestLoan(1: LoanRequest request)
    LoanResponse processLoanRequest(1: LoanRequest request)
}
struct LoanRequest {1: double amount}
enum LoanResponse {
    APPROVED,
    REJECTED,
}
