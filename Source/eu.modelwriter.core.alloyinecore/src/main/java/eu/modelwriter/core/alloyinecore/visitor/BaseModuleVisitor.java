package eu.modelwriter.core.alloyinecore.visitor;


import eu.modelwriter.core.alloyinecore.structure.*;
import eu.modelwriter.core.alloyinecore.structure.Class;
import eu.modelwriter.core.alloyinecore.structure.Enum;
import eu.modelwriter.core.alloyinecore.structure.Package;

public class BaseModuleVisitor<T extends Element> extends AbstractModuleVisitor<T> {

    @Override
    public T visitAnnotationReference(AnnotationReference annotationReference) {
        return visitChildren(annotationReference);
    }

    @Override
    public T visitOperation(Operation operation) {
        return visitChildren(operation);
    }

    @Override
    public T visitIntConstant(IntExpression.IntConstant intConstant) {
        return visitChildren(intConstant);
    }

    @Override
    public T visitPreCondition(PreCondition preCondition) {
        return visitChildren(preCondition);
    }

    @Override
    public T visitNot(Formula.Not not) {
        return visitChildren(not);
    }

    @Override
    public T visitProduct(Expression.Product product) {
        return visitChildren(product);
    }

    @Override
    public T visitDivide(IntExpression.Divide divide) {
        return visitChildren(divide);
    }

    @Override
    public T visitSumDeclaration(Formula.SumDeclaration sumDeclaration) {
        return visitChildren(sumDeclaration);
    }

    @Override
    public T visitEnumLiteral(EnumLiteral enumLiteral) {
        return visitChildren(enumLiteral);
    }

    @Override
    public T visitClosure(Expression.Closure closure) {
        return visitChildren(closure);
    }

    @Override
    public T visitOneOf(QuantifierDeclaration.OneOf oneOf) {
        return visitChildren(oneOf);
    }

    @Override
    public T visitFalse(Formula.False _false) {
        return visitChildren(_false);
    }

    @Override
    public T visitAnnotationDetail(AnnotationDetail annotationDetail) {
        return visitChildren(annotationDetail);
    }

    @Override
    public T visitModulo(IntExpression.Modulo modulo) {
        return visitChildren(modulo);
    }

    @Override
    public T visitInterface(Interface _interface) {
        return visitChildren(_interface);
    }

    @Override
    public T visitIfExpression(Expression.IfExpression ifExpression) {
        return visitChildren(ifExpression);
    }

    @Override
    public T visitBoxJoin(Expression.BoxJoin boxJoin) {
        return visitChildren(boxJoin);
    }

    @Override
    public T visitMultiply(IntExpression.Multiply multiply) {
        return visitChildren(multiply);
    }

    @Override
    public T visitGenericSuperType(GenericSuperType genericSuperType) {
        return visitChildren(genericSuperType);
    }

    @Override
    public T visitLone(Formula.Lone lone) {
        return visitChildren(lone);
    }

    @Override
    public T visitAnd(Formula.And and) {
        return visitChildren(and);
    }

    @Override
    public T visitInts(Expression.Ints ints) {
        return visitChildren(ints);
    }

    @Override
    public T visitImplies(Formula.Implies implies) {
        return visitChildren(implies);
    }

    @Override
    public T visitImport(Import _import) {
        return visitChildren(_import);
    }

    @Override
    public T visitEcoreImport(EcoreImport ecoreImport) {
        return visitChildren(ecoreImport);
    }

    @Override
    public T visitComprehensionDeclaration(ComprehensionDeclaration comprehensionDeclaration) {
        return visitChildren(comprehensionDeclaration);
    }

    @Override
    public T visitSetOf(QuantifierDeclaration.SetOf setOf) {
        return visitChildren(setOf);
    }

    @Override
    public T visitForNo(Formula.ForNo forNo) {
        return visitChildren(forNo);
    }

    @Override
    public T visitParameter(Parameter parameter) {
        return visitChildren(parameter);
    }

    @Override
    public T visitInitial(Initial initial) {
        return visitChildren(initial);
    }

    @Override
    public T visitTypeParameter(TypeParameter typeParameter) {
        return visitChildren(typeParameter);
    }

    @Override
    public T visitDerivation(Derivation derivation) {
        return visitChildren(derivation);
    }

    @Override
    public T visitPrimitiveType(PrimitiveType primitiveType) {
        return visitChildren(primitiveType);
    }

    @Override
    public T visitEnum(Enum _enum) {
        return visitChildren(_enum);
    }

    @Override
    public T visitIntersection(Expression.Intersection intersection) {
        return visitChildren(intersection);
    }

    @Override
    public T visitReflexive(Expression.Reflexive reflexive) {
        return visitChildren(reflexive);
    }

    @Override
    public T visitTrue(Formula.True _true) {
        return visitChildren(_true);
    }

    @Override
    public T visitCount(IntExpression.Count count) {
        return visitChildren(count);
    }

    @Override
    public T visitGenericException(GenericException genericException) {
        return visitChildren(genericException);
    }

    @Override
    public T visitLt(Formula.Lt lt) {
        return visitChildren(lt);
    }

    @Override
    public T visitRelationalOverride(Expression.RelationalOverride relationalOverride) {
        return visitChildren(relationalOverride);
    }

    @Override
    public T visitForAll(Formula.ForAll forAll) {
        return visitChildren(forAll);
    }

    @Override
    public T visitBody(Body body) {
        return visitChildren(body);
    }

    @Override
    public T visitGenericTypeArgument(GenericTypeArgument genericTypeArgument) {
        return visitChildren(genericTypeArgument);
    }

    @Override
    public T visitFunction(Formula.Function function) {
        return visitChildren(function);
    }

    @Override
    public T visitPlus(IntExpression.Plus plus) {
        return visitChildren(plus);
    }

    @Override
    public T visitJoin(Expression.Join join) {
        return visitChildren(join);
    }

    @Override
    public T visitUniv(Expression.Univ univ) {
        return visitChildren(univ);
    }

    @Override
    public T visitIfIntExpression(IntExpression.IfIntExpression ifIntExpression) {
        return visitChildren(ifIntExpression);
    }

    @Override
    public T visitInvariant(Invariant invariant) {
        return visitChildren(invariant);
    }

    @Override
    public T visitVariable(Variable variable) {
        return visitChildren(variable);
    }

    @Override
    public T visitDifference(Expression.Difference difference) {
        return visitChildren(difference);
    }

    @Override
    public T visitEqual(Formula.Equal equal) {
        return visitChildren(equal);
    }

    @Override
    public T visitVar(Expression.Var var) {
        return visitChildren(var);
    }

    @Override
    public T visitIff(Formula.Iff iff) {
        return visitChildren(iff);
    }

    @Override
    public T visitSome(Formula.Some some) {
        return visitChildren(some);
    }

    @Override
    public T visitGenericType(GenericType genericType) {
        return visitChildren(genericType);
    }

    @Override
    public T visitMinus(IntExpression.Minus minus) {
        return visitChildren(minus);
    }

    @Override
    public T visitEq(Formula.Eq eq) {
        return visitChildren(eq);
    }

    @Override
    public T visitMultiplicity(Multiplicity multiplicity) {
        return visitChildren(multiplicity);
    }

    @Override
    public T visitSomeOf(QuantifierDeclaration.SomeOf someOf) {
        return visitChildren(someOf);
    }

    @Override
    public T visitOr(Formula.Or or) {
        return visitChildren(or);
    }

    @Override
    public T visitGenericWildcard(GenericWildcard genericWildcard) {
        return visitChildren(genericWildcard);
    }

    @Override
    public T visitNone(Expression.None none) {
        return visitChildren(none);
    }

    @Override
    public T visitUnion(Expression.Union union) {
        return visitChildren(union);
    }

    @Override
    public T visitReference(Reference reference) {
        return visitChildren(reference);
    }

    @Override
    public T visitPackage(Package _package) {
        return visitChildren(_package);
    }

    @Override
    public T visitForSome(Formula.ForSome forSome) {
        return visitChildren(forSome);
    }

    @Override
    public T visitGenericElementType(GenericElementType genericElementType) {
        return visitChildren(genericElementType);
    }

    @Override
    public T visitComprehension(Expression.Comprehension comprehension) {
        return visitChildren(comprehension);
    }

    @Override
    public T visitGte(Formula.Gte gte) {
        return visitChildren(gte);
    }

    @Override
    public T visitClass(Class _class) {
        return visitChildren(_class);
    }

    @Override
    public T visitForOne(Formula.ForOne forOne) {
        return visitChildren(forOne);
    }

    @Override
    public T visitAttribute(Attribute attribute) {
        return visitChildren(attribute);
    }

    @Override
    public T visitSum(IntExpression.Sum sum) {
        return visitChildren(sum);
    }

    @Override
    public T visitGt(Formula.Gt gt) {
        return visitChildren(gt);
    }

    @Override
    public T visitTranspose(Expression.Transpose transpose) {
        return visitChildren(transpose);
    }

    @Override
    public T visitAnnotation(Annotation annotation) {
        return visitChildren(annotation);
    }

    @Override
    public T visitOne(Formula.One one) {
        return visitChildren(one);
    }

    @Override
    public T visitModule(Module module) {
        return visitChildren(module);
    }

    @Override
    public T visitRel(Expression.Rel rel) {
        return visitChildren(rel);
    }

    @Override
    public T visitLte(Formula.Lte lte) {
        return visitChildren(lte);
    }

    @Override
    public T visitLetDeclaration(LetDeclaration letDeclaration) {
        return visitChildren(letDeclaration);
    }

    @Override
    public T visitPostCondition(PostCondition postCondition) {
        return visitChildren(postCondition);
    }

    @Override
    public T visitTotalOrder(Formula.TotalOrder totalOrder) {
        return visitChildren(totalOrder);
    }

    @Override
    public T visitIn(Formula.In in) {
        return visitChildren(in);
    }

    @Override
    public T visitAcyclic(Formula.Acyclic acyclic) {
        return visitChildren(acyclic);
    }

    @Override
    public T visitForLone(Formula.ForLone forLone) {
        return visitChildren(forLone);
    }

    @Override
    public T visitNo(Formula.No no) {
        return visitChildren(no);
    }

    @Override
    public T visitLoneOf(QuantifierDeclaration.LoneOf loneOf) {
        return visitChildren(loneOf);
    }

    @Override
    public T visitIden(Expression.Iden iden) {
        return visitChildren(iden);
    }

    @Override
    public T visitDataType(DataType dataType) {
        return visitChildren(dataType);
    }

    @Override
    public T visitNotIn(Formula.NotIn notIn) {
        return visitChildren(notIn);
    }

    @Override
    public T visitNotEqual(Formula.NotEqual notEqual) {
        return visitChildren(notEqual);
    }

    @Override
    public T visitNotEq(Formula.NotEq notEq) {
        return visitChildren(notEq);
    }

    @Override
    public T visitNotLt(Formula.NotLt notLt) {
        return visitChildren(notLt);
    }

    @Override
    public T visitNotLte(Formula.NotLte notLte) {
        return visitChildren(notLte);
    }

    @Override
    public T visitNotGt(Formula.NotGt notGt) {
        return visitChildren(notGt);
    }

    @Override
    public T visitNotGte(Formula.NotGte notGte) {
        return visitChildren(notGte);
    }

    @Override
    public T visitPartialFunction(Formula.PartialFunction partialFunction) {
        return visitChildren(partialFunction);
    }
}
