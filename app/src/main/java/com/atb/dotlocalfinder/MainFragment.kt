/*
 *    Copyright 2020- Network Revolution Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.atb.dotlocalfinder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.atb.dotlocalfinder.databinding.MainFragmentBinding
import com.example.atb_app.models.Echo
import com.example.atb_app.models.Message
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.xbill.DNS.Type

class MainFragment : Fragment() {

    private lateinit var webSocket: WebSocket
    lateinit var binding: MainFragmentBinding

    var textToDisplay:String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = MainFragmentBinding.inflate(layoutInflater)
        //setContentView(binding.root)


        val mainViewModel by viewModels<MainViewModel>()
        val recyclerAdapter = MainRecyclerAdapter(object : MainRecyclerAdapter.OnCardClickListener {
            override fun onCardClicked(mainDataClass: MainDataClass) {
                val copyAddress = when (mainDataClass.resolveType) {
                    Type.AAAA -> "[${mainDataClass.ipAddress}]"
                    else -> mainDataClass.ipAddress
                }


                Log.e("copyAddress", copyAddress)

                val client = OkHttpClient()
                val request: Request = Request.Builder()
                    .url("ws://${copyAddress}/ws")
                    //.url("ws://atb2100.local/ws")
                    .build()

                Log.e("Request", request.toString())

                webSocket = client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        Log.i("INFO", "Connected")
                        sendRequestForAPavailable()
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {

                        //this@MainActivity?.runOnUiThread {


                        activity?.runOnUiThread {


                            textToDisplay = text

                            Snackbar.make(
                                view!!,
                                "textToDisplay: $textToDisplay",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            Log.e("textToDisplay", textToDisplay)

                            binding.terminalField.text = text
                            val p = Gson().fromJson(text, Echo::class.java)
                            Log.e("Received", text)
                            Log.e("Decerealized", p.toString())
                            Log.e("FID", p.FID)
                            Log.e("RID", p.RID)
                            Log.e("STA", p.ARG?.AP.toString())

                            if (p.ARG?.STA.toString().toInt() != 0) {
                                sendRequestForAPavailable()
                            } else {
                                binding.stationsFoundValueField.text = p.ARG?.AP.toString()


                                Snackbar.make(
                                    view!!,
                                    "Networks found: ${p.ARG?.AP.toString()}",
                                    Snackbar.LENGTH_SHORT
                                ).show()


                                Log.e("Networks found", p.ARG?.AP.toString())

                            }
                            //Log.e("ARG", p.ARG)
                        }
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        // Connection closing
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        // Connection closed
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        // Connection failed
                        Log.i("INFO", "onFailure")
                        //t.printStackTrace()
                    }
                })



                when (mainViewModel.actionList[mainViewModel.selectedPosition]) {
                    "Clipboard" -> {
                        val clipboard =
                            context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("mDNS", copyAddress))
                        // clipboard.primaryClip = ClipData.newPlainText("mDNS", mainDataClass.ipAddress))
                        Snackbar.make(
                            view!!,
                            "Copied the IP address : $copyAddress",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    "HTTP" -> {
                        val uri = Uri.parse("http://${copyAddress}/")
                        Intent(Intent.ACTION_VIEW, uri).also {
                            startActivity(it)
                        }
                    }

                    "HTTPS" -> {
                        val uri = Uri.parse("https://${copyAddress}/")
                        Intent(Intent.ACTION_VIEW, uri).also {
                            startActivity(it)
                        }
                    }
                }
            }
        })
        mainViewModel.liveDataList.observe(viewLifecycleOwner) {
            it?.also {
                recyclerAdapter.dataList = it
                recyclerAdapter.notifyDataSetChanged()
            }
        }

        MainFragmentBinding.inflate(inflater, container, false).also {
            it.viewModel = mainViewModel
            it.itemAdapter = recyclerAdapter
            return it.root
        }
    }

    fun sendRequestForAPavailable() {
        val publish = Gson().toJson(Message(FID = "E1F15CA1", RID = "4442"))
//val publish = Gson().toJson(Message("global", "publish", message))
        Log.i("Message", publish)
        webSocket.send(publish)

    }
}
