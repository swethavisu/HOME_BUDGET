package swetha.homebudgeting.homebudget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import swetha.homebudgeting.homebudget.databinding.ActivityAddTransactionBinding

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        binding.labelInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                binding.labellayout.error = null
        }
        binding.amountInput.addTextChangedListener {
            if (it!!.isNotEmpty())
                binding.amountlayout.error = null
        }

        binding.addTransactionBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                binding.labellayout.error = "Please enter a valid label"

            else if (amount == null)
                binding.amountlayout.error = "Please enter a valid amount"
            else {
                val transaction=Transaction(0, label, amount,description )
                insert(transaction)
            }
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }
    private fun insert(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}