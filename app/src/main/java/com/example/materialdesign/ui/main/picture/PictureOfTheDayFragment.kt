package com.example.materialdesign.ui.main.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Log.DEBUG
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.materialdesign.BuildConfig.DEBUG
import com.example.materialdesign.MainActivity
import com.example.materialdesign.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.main_fragment.*

class PictureOfTheDayFragment : Fragment() {


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
            Log.d("", "Открываем Wiki")
        }
        setBottomAppBar(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
        Log.d("", "onCreateOptionsMenu")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show()
            R.id.app_bar_search -> Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity  // поиск контекста
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar)) // указываем, что в роли экшнбара выступает наш боттонбар
        setHasOptionsMenu(true)
        Log.d("", "onCreateOptionsMenu")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData().observe(this@PictureOfTheDayFragment,   // предлагает viewLifecycleOwner
            Observer<PictureOfTheDayData> { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    //Отобразите ошибку
                    //showError("Сообщение, что ссылка пустая")
                } else {
                    //Отобразите фото
                    //showSuccess()
                    //Coil в работе: достаточно вызвать у нашего ImageView
                    //нужную extension-функцию и передать ссылку и заглушки для placeholder
                    image_view.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                //Отобразите загрузку
                //showLoading()
            }
            is PictureOfTheDayData.Error -> {
            }
        }
    }

    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet) // получаем из нашего контейнера для bottom_sheet_layout (app:layout_behavior="com.google.android.material.bottomsheet.BottomSheetBehavior")
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED // положение "свёрнуто"
    }

}
