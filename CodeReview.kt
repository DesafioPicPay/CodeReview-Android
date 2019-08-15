import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class PicPayActivity : Activity() {

    lateinit var viewModel: PicPayViewModel
    lateinit var transactionList: RecyclerView

    var transactions: MutableList<Transaction>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teste_layout)

        viewModel = PicPayViewModel()
        
        transactionList = findViewById(R.id.transacoes)
        transactionList.run {
            layoutManager = LinearLayoutManager(this@PicPayActivity, LinearLayout.VERTICAL, false)
            setHasFixedSize(true)
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.loadTransactions()
                .subscribe(
                        {response ->
                            val transactionsFiltered = response!!.filter{ it!!.status = true }
                            transactions?.addAll(transactionsFiltered)
                            var adapter = TransactionsAdapter(transactions!!)
                            transactionList.adapter = adapter
                        },
                        { Toast.makeText(this, "Error = " + it.message, Toast.LENGTH_SHORT).show() }
                )
    }
}

class TransactionsAdapter(private val dataSource: List<Transaction>) :
        RecyclerView.Adapter<TransactionsAdapter.ContactViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ContactViewHolder {
        // inflate and return view
        TODO()
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        // bind views
    }

    override fun getItemCount() = dataSource.size

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view)
}

class PicPayViewModel: ViewModel() {
    private val service = Service()
    
    fun loadTransactions() : Single<List<Transaction>?> {
      return service.loadTransactions()   
    }
}

class Service {
    fun loadTransactions(): Single<List<Transaction>?> {
        return Single.just(listOf(
                Transaction(),
                Transaction(),
                Transaction()
        )).delay(1000, TimeUnit.MILLISECONDS)
    }
}


class Transaction {
    var id: Long = 0L
    var name: String = ""
    var value: Double = 0.0
    var status: Boolean
}



///Layout 

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:orientation="vertical"
    android:background="#F0F0F0"
    android:layout_height="match_parent">

    <LinearLayout
        android:id="@+id/header"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:orientation="horizontal">

        <ImageView
            android:layout_width="60dp"
            android:layout_height="wrap_content"
            android:layout_margin="10dp"
            android:src="@drawable/avatar_person"/>

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight=".3"
            android:orientation="vertical">

            <TextView
                android:id="@+id/nome"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:text="nome"/>

            <TextView
                android:id="@+id/sobrenome"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:text="sobrenome"/>

        </LinearLayout>


    </LinearLayout>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="60dp"
        android:background="@android:color/darker_gray"
        android:gravity="center"
        android:text="Lista de transações"/>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/header"
        android:layout_marginTop="20dp"
        android:orientation="vertical">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/transacoes"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical" />

    </LinearLayout>

</LinearLayout>
