package io.unthrottled.doki.settings.actors

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import io.unthrottled.doki.config.ThemeConfig
import io.unthrottled.doki.notification.UpdateNotification
import io.unthrottled.doki.promotions.MessageBundle
import io.unthrottled.doki.stickers.EditorBackgroundWallpaperService
import kotlin.reflect.full.functions

object BackgroundActor {
  fun handleBackgroundUpdate(enabled: Boolean) {
    if (enabled != ThemeConfig.instance.isDokiBackground) {
      ThemeConfig.instance.isDokiBackground = enabled
      if (enabled) {
        EditorBackgroundWallpaperService.instance.enableEditorBackground()
        UpdateNotification.showNotificationAcrossProjects(
          MessageBundle.message("wallpaper.install.title"),
          MessageBundle.message("wallpaper.install.body"),
          actions =
            listOf {
              object : NotificationAction(MessageBundle.message("wallpaper.install.show-settings")) {
                override fun actionPerformed(
                  e: AnActionEvent,
                  notification: Notification,
                ) {
                  val action = ActionManager.getInstance().getAction("Images.SetBackgroundImage")
                  action.javaClass.methods.firstOrNull {
                    it.name == "actionPerformed" && it.parameterCount == 1
                  }?.apply {
                    invoke(action, e)
                  }
                }
              }
            },
        )
      } else {
        EditorBackgroundWallpaperService.instance.removeEditorBackground()
      }
    }
  }
}
