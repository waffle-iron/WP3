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

package eu.modelwriter.core.alloyinecore.structure.instance;

import eu.modelwriter.core.alloyinecore.recognizer.AlloyInEcoreParser.SlotContext;
import eu.modelwriter.core.alloyinecore.structure.base.Element;
import eu.modelwriter.core.alloyinecore.structure.model.StructuralFeature;
import eu.modelwriter.core.alloyinecore.visitor.IVisitor;
import org.antlr.v4.runtime.RuleContext;

import java.util.stream.Collectors;

public final class Slot extends Element<SlotContext> {
    public Slot(SlotContext context) {
        super(context);
    }

    public StructuralFeature getDefiningFeature() {
        return null;
    }

    @Override
    public String getLabel() {
        int start;
        int stop;
        if (getContext().name != null) {
            start = getContext().name.start.getStartIndex();
            stop = getContext().name.stop.getStopIndex();
        } else {
            start = getContext().start.getStartIndex();
            stop = getContext().start.getStopIndex();
        }
        return  Element.getNormalizedText(getContext(), start, stop);
    }

    @Override
    public String getSuffix() {
        if (!getContext().eObject().isEmpty()) {
            return ": { " + String.join(", " , this.getOwnedElements(Object.class).stream().map(object -> object.getLabel().concat(object.getSuffix())).collect(Collectors.toList()) ) + " }";
        } else if (!getContext().eObjectValue().isEmpty()) {
            return ": [ " + String.join(", " , this.getOwnedElements(ObjectValue.class).stream().map(object -> object.getLabel().concat(object.getSuffix())).collect(Collectors.toList()) ) + " ]";
        } else if (getContext().dataValue() != null){
            int start = getContext().dataValue().start.getStartIndex();
            int stop = getContext().dataValue().stop.getStopIndex();
            return ": " + Element.getNormalizedText(getContext(), start, stop);
        } else {
            return "";
        }
    }

    @Override
    public <T> T accept(IVisitor<? extends T> visitor) {
        return visitor.visitSlot(this);
    }
}
