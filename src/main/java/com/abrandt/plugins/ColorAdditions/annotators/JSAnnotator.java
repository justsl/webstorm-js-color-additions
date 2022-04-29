/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.abrandt.plugins.ColorAdditions.annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"DuplicateStringLiteralInspection",
        "SwitchStatement",
        "HardCodedStringLiteral",
        "SwitchStatementWithTooManyBranches",
        "OverlyLongMethod"})
public final class JSAnnotator implements Annotator {
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public void annotate(@NotNull final PsiElement element, @NotNull final AnnotationHolder holder) {
        if (element instanceof LeafPsiElement) {
            if (PsiTreeUtil.getParentOfType(element, PsiComment.class) != null) {
                return;
            }

            final TextAttributesKey kind = getKeywordKind(element);
            if (kind == null) {
                return;
            }
            final TextRange textRange = element.getTextRange();
            final TextRange range = new TextRange(textRange.getStartOffset(), textRange.getEndOffset());
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range)
                    .textAttributes(kind)
                    .create();
        }
    }

    public static final TextAttributesKey JS_KEYWORD = ObjectUtils.notNull(TextAttributesKey.find("JS.KEYWORD"),
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey JS_NUMBER = ObjectUtils.notNull(TextAttributesKey.find("JS.NUMBER"),
            DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey THIS_SUPER = TextAttributesKey.createTextAttributesKey("JS.THIS_SUPER", JS_KEYWORD);
    public static final TextAttributesKey MODULE = TextAttributesKey.createTextAttributesKey("JS.MODULE_KEYWORD", JS_KEYWORD);
    public static final TextAttributesKey DEBUGGER = TextAttributesKey.createTextAttributesKey("JS.DEBUGGER_STMT", JS_KEYWORD);
    public static final TextAttributesKey CONSOLE = TextAttributesKey.createTextAttributesKey("JS.CONSOLE", JS_KEYWORD);
    public static final TextAttributesKey NULL = TextAttributesKey.createTextAttributesKey("JS.NULL_UNDEFINED", JS_NUMBER);
    public static final TextAttributesKey VAL = TextAttributesKey.createTextAttributesKey("JS.VAR_DEF", JS_KEYWORD);
    public static final TextAttributesKey FUNCTION = TextAttributesKey.createTextAttributesKey("JS.FUNCTION", JS_KEYWORD);
    public static final TextAttributesKey PRIMITIVE = TextAttributesKey.createTextAttributesKey("JS.PRIMITIVE", JS_NUMBER);

    @Nullable
    protected TextAttributesKey getKeywordKind(@NotNull final PsiElement element) {
        TextAttributesKey kind = null;
        switch (element.getText()) {
            case "this":
            case "super":
                kind = THIS_SUPER;
                break;
            case "export":
            case "import":
            case "require":
            case "from":
            case "module":
                kind = MODULE;
                break;
            case "debugger":
                kind = DEBUGGER;
                break;
            case "null":
            case "undefined":
                kind = NULL;
                break;
            case "true":
            case "false":
                kind = PRIMITIVE;
                break;
            case "var":
            case "let":
            case "const":
                kind = VAL;
                break;
            case "function":
                kind = FUNCTION;
                break;
            case "console":
                kind = CONSOLE;
                break;
            default:
                break;
        }
        return kind;
    }
}
