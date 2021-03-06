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

// Generated from /home/ferhat/IdeaProjects/eu.modelwriter.core.alloyinecore/src/main/java/eu/modelwriter/core/alloyinecore/recognizer/imports/Imports.g4 by ANTLR 4.6
package eu.modelwriter.core.alloyinecore.recognizer.imports;

import eu.modelwriter.core.alloyinecore.structure.base.Element;
import eu.modelwriter.core.alloyinecore.structure.imports.ImportedPackage;
import eu.modelwriter.core.alloyinecore.structure.imports.ImportedClass;
import eu.modelwriter.core.alloyinecore.structure.imports.ImportedInterface;
import eu.modelwriter.core.alloyinecore.structure.imports.ImportedDataType;
import eu.modelwriter.core.alloyinecore.structure.imports.ImportedEnum;
import eu.modelwriter.core.alloyinecore.structure.model.Import;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ImportsParser}.
 */
public interface ImportsListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ImportsParser#importModel}.
	 * @param ctx the parse tree
	 */
	void enterImportModel(ImportsParser.ImportModelContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#importModel}.
	 * @param ctx the parse tree
	 */
	void exitImportModel(ImportsParser.ImportModelContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iPackage}.
	 * @param ctx the parse tree
	 */
	void enterIPackage(ImportsParser.IPackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iPackage}.
	 * @param ctx the parse tree
	 */
	void exitIPackage(ImportsParser.IPackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iClassifier}.
	 * @param ctx the parse tree
	 */
	void enterIClassifier(ImportsParser.IClassifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iClassifier}.
	 * @param ctx the parse tree
	 */
	void exitIClassifier(ImportsParser.IClassifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iClass}.
	 * @param ctx the parse tree
	 */
	void enterIClass(ImportsParser.IClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iClass}.
	 * @param ctx the parse tree
	 */
	void exitIClass(ImportsParser.IClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iDataType}.
	 * @param ctx the parse tree
	 */
	void enterIDataType(ImportsParser.IDataTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iDataType}.
	 * @param ctx the parse tree
	 */
	void exitIDataType(ImportsParser.IDataTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#visibility}.
	 * @param ctx the parse tree
	 */
	void enterVisibility(ImportsParser.VisibilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#visibility}.
	 * @param ctx the parse tree
	 */
	void exitVisibility(ImportsParser.VisibilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(ImportsParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(ImportsParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#templateSignature}.
	 * @param ctx the parse tree
	 */
	void enterTemplateSignature(ImportsParser.TemplateSignatureContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#templateSignature}.
	 * @param ctx the parse tree
	 */
	void exitTemplateSignature(ImportsParser.TemplateSignatureContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iTypeParameter}.
	 * @param ctx the parse tree
	 */
	void enterITypeParameter(ImportsParser.ITypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iTypeParameter}.
	 * @param ctx the parse tree
	 */
	void exitITypeParameter(ImportsParser.ITypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iGenericType}.
	 * @param ctx the parse tree
	 */
	void enterIGenericType(ImportsParser.IGenericTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iGenericType}.
	 * @param ctx the parse tree
	 */
	void exitIGenericType(ImportsParser.IGenericTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iGenericTypeArgument}.
	 * @param ctx the parse tree
	 */
	void enterIGenericTypeArgument(ImportsParser.IGenericTypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iGenericTypeArgument}.
	 * @param ctx the parse tree
	 */
	void exitIGenericTypeArgument(ImportsParser.IGenericTypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#iGenericWildcard}.
	 * @param ctx the parse tree
	 */
	void enterIGenericWildcard(ImportsParser.IGenericWildcardContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#iGenericWildcard}.
	 * @param ctx the parse tree
	 */
	void exitIGenericWildcard(ImportsParser.IGenericWildcardContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#pathName}.
	 * @param ctx the parse tree
	 */
	void enterPathName(ImportsParser.PathNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#pathName}.
	 * @param ctx the parse tree
	 */
	void exitPathName(ImportsParser.PathNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#segment}.
	 * @param ctx the parse tree
	 */
	void enterSegment(ImportsParser.SegmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#segment}.
	 * @param ctx the parse tree
	 */
	void exitSegment(ImportsParser.SegmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#unrestrictedName}.
	 * @param ctx the parse tree
	 */
	void enterUnrestrictedName(ImportsParser.UnrestrictedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#unrestrictedName}.
	 * @param ctx the parse tree
	 */
	void exitUnrestrictedName(ImportsParser.UnrestrictedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ImportsParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(ImportsParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ImportsParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(ImportsParser.IdentifierContext ctx);
}