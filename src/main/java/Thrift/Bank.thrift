namespace java Thrift.src
service BankService {
    double borrowMoney(1: double amount);
    void repayLoan(1: double amount);
}
