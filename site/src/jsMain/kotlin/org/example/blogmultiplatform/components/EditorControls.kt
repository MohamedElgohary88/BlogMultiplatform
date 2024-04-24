package org.example.blogmultiplatform.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.layout.SimpleGrid
import com.varabyte.kobweb.silk.components.layout.numColumns
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import kotlinx.browser.document
import org.example.blogmultiplatform.models.EditorControl
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.Id
import org.example.blogmultiplatform.utils.applyControlStyle
import org.example.blogmultiplatform.utils.getEditor
import org.example.blogmultiplatform.utils.noBorder
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button

@Composable
fun EditorControls(
    breakpoint: Breakpoint,
    editorVisibility: Boolean,
    onEditorVisibilityChange: () -> Unit,
    onLinkClick: () -> Unit,
    onImageClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SimpleGrid(
            modifier = Modifier.fillMaxWidth(),
            numColumns = numColumns(base = 1, sm = 2)
        ) {
            Row(
                modifier = Modifier
                    .backgroundColor(Theme.LightGray.rgb)
                    .borderRadius(r = 4.px)
                    .height(54.px)
            ) {
                EditorControl.entries.forEach {
                    EditorKeyView(
                        control = it,
                        onClick = {
                            applyControlStyle(
                                it,
                                onLinkClick,
                                onImageClick
                            )
                        })
                }
            }
            Box(contentAlignment = Alignment.CenterEnd) {
                Button(
                    attrs = Modifier
                        .height(54.px)
                        .thenIf(
                            condition = breakpoint < Breakpoint.SM,
                            other = Modifier.fillMaxWidth()
                        )
                        .margin(topBottom = if (breakpoint < Breakpoint.SM) 12.px else 0.px)
                        .padding(leftRight = 24.px)
                        .borderRadius(r = 4.px)
                        .backgroundColor(Theme.LightGray.rgb)
                        .color(Theme.DarkGray.rgb)
                        .backgroundColor(
                            if (editorVisibility) Theme.LightGray.rgb
                            else Theme.Primary.rgb
                        )
                        .color(
                            if (editorVisibility) Theme.DarkGray.rgb
                            else Colors.White
                        )
                        .noBorder()
                        .onClick {
                            onEditorVisibilityChange()
                            document.getElementById(Id.editorPreview)?.innerHTML = getEditor().value
                            js("hljs.highlightAll()") as Unit
                        }
                        .toAttrs()
                ) {
                    SpanText(
                        modifier = Modifier
                            .fontFamily(FONT_FAMILY)
                            .fontWeight(FontWeight.Medium)
                            .fontSize(14.px),
                        text = "Preview"
                    )
                }
            }
        }
    }
}