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

package eu.modelwriter.core.alloyinecore.recognizer;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.emf.ecore.*;
import eu.modelwriter.core.alloyinecore.recognizer.AlloyInEcoreParser.EAttributeContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document {

    final List<NamedElement> elements = new ArrayList<>();
    final Map<String, NamedElement> qNameStore = new HashMap<>();

    AlloyInEcoreParser parser = null;

    private static Document ourInstance = new Document();

    public static Document getInstance() {
        return ourInstance;
    }

    private Document() { }

    void addNamedElement(ENamedElement element, ParserRuleContext context, Token nameToken){
        elements.add(new NamedElement(element, context, nameToken));
    }

    private static String getQualifiedName(ENamedElement e){
        String qname = null;
        if (e instanceof EPackage)
            qname = Document.getQualifiedName((EPackage) e);
        else if (e instanceof EClassifier)
            qname = Document.getQualifiedName((EClassifier) e);
        else if (e instanceof EStructuralFeature)
            qname = Document.getQualifiedName((EStructuralFeature) e);
        else if (e instanceof EOperation)
            qname = Document.getQualifiedName((EOperation) e);
        else if (e instanceof EParameter)
            qname = Document.getQualifiedName((EParameter) e);
        else if (e instanceof EEnumLiteral)
            qname = Document.getQualifiedName((EEnumLiteral) e);
        return qname;
    }

    private static String getQualifiedName(EPackage p) {
        return p.getESuperPackage() == null ? p.getName() : getQualifiedName(p.getESuperPackage()) + "." + p.getName();
    }

    private static String getQualifiedName(EClassifier c) {
        return getQualifiedName(c.getEPackage()) + "." + c.getName();
    }

    private static String getQualifiedName(EStructuralFeature f) {
        return getQualifiedName(f.getEContainingClass()) + "::" + f.getName();
    }

    private static String getQualifiedName(EOperation o) {
        return getQualifiedName(o.getEContainingClass()) + "->" + o.getName();
    }

    private static String getQualifiedName(EEnumLiteral l) {
        return getQualifiedName(l.getEEnum()) + "::" + l.getName();
    }

    private static String getQualifiedName(EParameter p) {
        return getQualifiedName(p.getEOperation()) + "::" + p.getName();
    }

    private void generateQNames(){
        for (NamedElement e: elements){
            e.qName = Document.getQualifiedName(e.element);
            String className = e.element.getClass().getSimpleName();
            className = className.substring(0, className.indexOf("Impl"));
            parser.notifyErrorListeners(e.token, "\t " + className + ": " + e.qName, null);
            qNameStore.put(e.qName, e);
        }
    }


    void signalParsingCompletion() {
        System.out.println("[NamedElement]");
      //  generateQNames();
    }

    class NamedElement
    {
        final ENamedElement element;
        final ParserRuleContext context;
        final Token token;
        String qName = null;

        NamedElement(ENamedElement element, ParserRuleContext context, Token nameToken) {
            this.element = element;
            this.context = context;
            this.token = nameToken;
        }
    }

    class AttributeType
    {
        EAttributeContext ownerContext;
        Token token;
        String targetQName = null;
    }

    class ReferenceType
    {
        EReference owner;
        EDataType targetElement;
        Token token;
        String targetQName = null;
    }

    class Opposite
    {
        EReference owner;
        EReference targetElement;
        Token token;
        String targetQName = null;
    }

    class SuperType
    {

    }

}