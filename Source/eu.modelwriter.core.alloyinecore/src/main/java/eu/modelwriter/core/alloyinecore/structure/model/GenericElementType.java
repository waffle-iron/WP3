/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017, Ferhat Erata <ferhat@computer.org>
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
 */

package eu.modelwriter.core.alloyinecore.structure.model;

import eu.modelwriter.core.alloyinecore.recognizer.AlloyInEcoreParser.EGenericElementTypeContext;
import eu.modelwriter.core.alloyinecore.structure.base.INavigable;
import eu.modelwriter.core.alloyinecore.structure.base.Object;
import eu.modelwriter.core.alloyinecore.visitor.IVisitor;
import org.antlr.v4.runtime.Token;
import org.eclipse.emf.ecore.EGenericType;

//GenericElementType
public final class GenericElementType extends Object<EGenericType, EGenericElementTypeContext> implements INavigable {
    public GenericElementType(EGenericType eObject, EGenericElementTypeContext context) {
        super(eObject, context);
    }

    public GenericElementType(EGenericElementTypeContext context) {
        super(context);
    }

    @Override
    public <T> T accept(IVisitor<? extends T> visitor) {
        return visitor.visitGenericElementType(this);
    }

    @Override
    public String getPathName() {
        if (this.getContext().eGenericType() != null && this.getContext().eGenericType().pathName() != null) {
            return this.getContext().eGenericType().pathName().getText();
        } else if (this.getContext().ePrimitiveType() != null){
                return this.getContext().ePrimitiveType().getText();
        } else
            return "";
    }

    @Override
    public Token getToken() {
        if (this.getContext().ePrimitiveType != null)
            return super.getToken();
        else if (getContext().eGenericType != null && getContext().eGenericType.ownedPathName != null)
            return getContext().eGenericType.ownedPathName.stop;
        else return null;
    }
}
