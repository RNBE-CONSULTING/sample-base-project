package fr.rnbe.android.core.models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

data class SnackMessageView @JvmOverloads constructor(
        val messageRes: Int,
        val action: MessageAction? = null
) {
    fun e(): MessageView {
        return convertTo(MessageLevel.Error)
    }

    fun w(): MessageView {
        return convertTo(MessageLevel.Warning)
    }

    fun i(): MessageView {
        return convertTo(MessageLevel.Default)
    }

    private fun convertTo(l: MessageLevel): MessageView {
        val bar = if (action != null) {
            MessageType.SnackBar(Snackbar.LENGTH_INDEFINITE)
        } else {
            MessageType.SnackBar(Snackbar.LENGTH_LONG)
        }

        return MessageView(messageRes = messageRes, type = bar, level = l, action = action)
    }
}

data class MessageView @JvmOverloads constructor(
        val messageStr: String? = null,
        val messageRes: Int? = null,
        val type: MessageType = MessageType.SnackBar(Snackbar.LENGTH_LONG),
        val level: MessageLevel = MessageLevel.Default,
        val action: MessageAction? = null
) {

    fun getMessage(context: Context? = null): String {
        return if (messageRes != null && context != null) {
            context.getString(messageRes)
        } else messageStr ?: ""
    }

    class Builder {
        private var _message: String? = null
        @StringRes private var _messageRes: Int? = null
        private var _type: MessageType = MessageType.SnackBar(Snackbar.LENGTH_LONG)
        private var _level: MessageLevel = MessageLevel.Default
        private var _action: MessageAction? = null

        fun setMessage(value: String) = this.also {
            _message = value
        }

        fun setMessage(@StringRes value: Int) = this.also {
            _messageRes = value
        }

        fun setLevel(value: MessageLevel) = this.also {
            _level = value
        }

        fun setType(value: MessageType, action: MessageAction? = null) = this.also {
            _type = value
            if (_type is MessageType.SnackBar) {
                _action = action
            } else if (action != null) {
                Log.w("MessageView.Builder", "action is not compatible with toast")
            }
        }

        fun build() = MessageView(_message, _messageRes, _type, _level, _action)
    }
}

data class MessageAction(@StringRes val title: Int, val run: () -> Unit?)

sealed class MessageType(val duration: Int) {
    data class SnackBar(var d: Int) : MessageType(d)
    object ToastShort : MessageType(Toast.LENGTH_SHORT)
    object ToastLong : MessageType(Toast.LENGTH_LONG)
}

enum class MessageLevel {
    Default, Warning, Error
}