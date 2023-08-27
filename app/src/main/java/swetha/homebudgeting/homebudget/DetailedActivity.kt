package swetha.homebudgeting.homebudget

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import swetha.homebudgeting.homebudget.databinding.ActivityDetailedBinding


class DetailedActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailedBinding
    private lateinit var transaction:Transaction


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(binding.root)

        transaction = intent.getSerializableExtra("transaction") as Transaction

        binding.labelInput.setText(transaction.label)
        binding.amountInput.setText(transaction.amount.toString())
        binding.descriptionInput.setText(transaction.description)

        binding.rootView.setOnClickListener{
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.labelInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                binding.labellayout.error = null
        }
        binding.amountInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE
            if (it!!.isNotEmpty())
                binding.amountlayout.error = null
        }
        binding.descriptionInput.addTextChangedListener {
            binding.updateBtn.visibility = View.VISIBLE

        }
        binding.updateBtn.setOnClickListener {
            val label = binding.labelInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val amount = binding.amountInput.text.toString().toDoubleOrNull()

            if (label.isEmpty())
                binding.labellayout.error = "Please enter a valid label"

            else if (amount == null)
                binding.amountlayout.error = "Please enter a valid amount"
            else {
                val transaction = Transaction(transaction.id, label, amount,description )
                update(transaction)
            }
        }
        binding.closeBtn.setOnClickListener {
            finish()
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    private fun update(transaction: Transaction){
        val db = Room.databaseBuilder(this,
            AppDatabase::class.java,
            "transactions").build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}
