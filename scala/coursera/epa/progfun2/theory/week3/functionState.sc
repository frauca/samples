object functionState {

  class BankAccount {
    private var balance = 0

    def deposit(amount: Int): Unit = {
      if (amount > 0) balance = balance + amount
    }

    def withdraw(amount: Int): Int =
      if (0 < amount && amount <= balance) {
        balance = balance - amount
        balance
      } else throw new Error("insufficient funds")
  }
  val account = new BankAccount // account: BankAccount = BankAccount
  account deposit 50 //
  account withdraw 20 // res1: Int = 30
  account withdraw 20 // res2: Int = 10
  account withdraw 15
}