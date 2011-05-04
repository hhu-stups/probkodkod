/**
 * 
 */
package de.stups.probkodkod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import kodkod.ast.Decl;
import kodkod.ast.Decls;
import kodkod.ast.Expression;
import kodkod.ast.Formula;
import kodkod.ast.IntConstant;
import kodkod.ast.IntExpression;
import kodkod.ast.Relation;
import kodkod.ast.Variable;
import kodkod.ast.operator.ExprCompOperator;
import kodkod.ast.operator.ExprOperator;
import kodkod.ast.operator.FormulaOperator;
import kodkod.ast.operator.IntCastOperator;
import kodkod.ast.operator.IntCompOperator;
import kodkod.ast.operator.IntOperator;
import kodkod.ast.operator.Multiplicity;
import kodkod.ast.operator.Quantifier;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import de.prob.prolog.output.IPrologTermOutput;
import de.stups.probkodkod.parser.analysis.DepthFirstAdapter;
import de.stups.probkodkod.parser.node.AAddIntexprBinop;
import de.stups.probkodkod.parser.node.AAllQuantifier;
import de.stups.probkodkod.parser.node.AAndInnerformula;
import de.stups.probkodkod.parser.node.AArgument;
import de.stups.probkodkod.parser.node.ABinaryInnerexpression;
import de.stups.probkodkod.parser.node.ABinaryInnerformula;
import de.stups.probkodkod.parser.node.ABinaryInnerintexpression;
import de.stups.probkodkod.parser.node.ABitpart;
import de.stups.probkodkod.parser.node.ACardInnerintexpression;
import de.stups.probkodkod.parser.node.ACastInnerexpression;
import de.stups.probkodkod.parser.node.ACastInnerintexpression;
import de.stups.probkodkod.parser.node.AClosureExprUnop;
import de.stups.probkodkod.parser.node.ACompInnerexpression;
import de.stups.probkodkod.parser.node.AConsDecls;
import de.stups.probkodkod.parser.node.AConstInnerexpression;
import de.stups.probkodkod.parser.node.AConstInnerformula;
import de.stups.probkodkod.parser.node.AConstInnerintexpression;
import de.stups.probkodkod.parser.node.ADiffExprBinop;
import de.stups.probkodkod.parser.node.AEmptyExprConst;
import de.stups.probkodkod.parser.node.AEqualsIntCompOp;
import de.stups.probkodkod.parser.node.AEqualsLogopRel;
import de.stups.probkodkod.parser.node.AExactReltype;
import de.stups.probkodkod.parser.node.AExistsQuantifier;
import de.stups.probkodkod.parser.node.AFalseLogConst;
import de.stups.probkodkod.parser.node.AFuncInnerformula;
import de.stups.probkodkod.parser.node.AGreaterIntCompOp;
import de.stups.probkodkod.parser.node.AGreaterequalIntCompOp;
import de.stups.probkodkod.parser.node.AIdenExprConst;
import de.stups.probkodkod.parser.node.AIfInnerexpression;
import de.stups.probkodkod.parser.node.AIffLogopBinary;
import de.stups.probkodkod.parser.node.AImpliesLogopBinary;
import de.stups.probkodkod.parser.node.AInLogopRel;
import de.stups.probkodkod.parser.node.AIntInnerformula;
import de.stups.probkodkod.parser.node.AInterExprMultop;
import de.stups.probkodkod.parser.node.AIntsType;
import de.stups.probkodkod.parser.node.AIntsetExprCast;
import de.stups.probkodkod.parser.node.AJoinExprBinop;
import de.stups.probkodkod.parser.node.ALesserIntCompOp;
import de.stups.probkodkod.parser.node.ALesserequalIntCompOp;
import de.stups.probkodkod.parser.node.AList;
import de.stups.probkodkod.parser.node.ALoneMultiplicity;
import de.stups.probkodkod.parser.node.AMulIntexprBinop;
import de.stups.probkodkod.parser.node.AMultInnerformula;
import de.stups.probkodkod.parser.node.AMultiInnerexpression;
import de.stups.probkodkod.parser.node.ANegZnumber;
import de.stups.probkodkod.parser.node.ANoMultiplicity;
import de.stups.probkodkod.parser.node.ANotInnerformula;
import de.stups.probkodkod.parser.node.AOneMultiplicity;
import de.stups.probkodkod.parser.node.AOrLogopBinary;
import de.stups.probkodkod.parser.node.AOverwriteExprBinop;
import de.stups.probkodkod.parser.node.APartialLogopFunction;
import de.stups.probkodkod.parser.node.APosReqtype;
import de.stups.probkodkod.parser.node.APosZnumber;
import de.stups.probkodkod.parser.node.APow2ExprCast;
import de.stups.probkodkod.parser.node.APowpart;
import de.stups.probkodkod.parser.node.APrjInnerexpression;
import de.stups.probkodkod.parser.node.AProblem;
import de.stups.probkodkod.parser.node.AProductExprMultop;
import de.stups.probkodkod.parser.node.AQuantInnerformula;
import de.stups.probkodkod.parser.node.AReflclsExprUnop;
import de.stups.probkodkod.parser.node.ARelInnerformula;
import de.stups.probkodkod.parser.node.ARelation;
import de.stups.probkodkod.parser.node.ARelrefInnerexpression;
import de.stups.probkodkod.parser.node.ARequest;
import de.stups.probkodkod.parser.node.ASetMultiplicity;
import de.stups.probkodkod.parser.node.ASomeMultiplicity;
import de.stups.probkodkod.parser.node.AStandardType;
import de.stups.probkodkod.parser.node.AStop;
import de.stups.probkodkod.parser.node.ASubIntexprBinop;
import de.stups.probkodkod.parser.node.ASubsetReltype;
import de.stups.probkodkod.parser.node.ATotalLogopFunction;
import de.stups.probkodkod.parser.node.ATransposeExprUnop;
import de.stups.probkodkod.parser.node.ATrueLogConst;
import de.stups.probkodkod.parser.node.ATuple;
import de.stups.probkodkod.parser.node.ATupleset;
import de.stups.probkodkod.parser.node.AUnaryInnerexpression;
import de.stups.probkodkod.parser.node.AUnionExprMultop;
import de.stups.probkodkod.parser.node.AUnivExprConst;
import de.stups.probkodkod.parser.node.AVarrefInnerexpression;
import de.stups.probkodkod.parser.node.PArgument;
import de.stups.probkodkod.parser.node.PDecls;
import de.stups.probkodkod.parser.node.PExpression;
import de.stups.probkodkod.parser.node.PFormula;
import de.stups.probkodkod.parser.node.PLogopFunction;
import de.stups.probkodkod.parser.node.PRelation;
import de.stups.probkodkod.parser.node.PReltype;
import de.stups.probkodkod.parser.node.PReqtype;
import de.stups.probkodkod.parser.node.PTuple;
import de.stups.probkodkod.parser.node.PTupleset;
import de.stups.probkodkod.parser.node.PType;
import de.stups.probkodkod.parser.node.PZnumber;
import de.stups.probkodkod.parser.node.Start;
import de.stups.probkodkod.parser.node.TIdentifier;
import de.stups.probkodkod.parser.node.TNumber;
import de.stups.probkodkod.types.TupleType;
import de.stups.probkodkod.types.Type;

/**
 * This is a visitor for the syntax tree of an input to the program.
 * 
 * The translated command is sent to the given {@link KodkodSession}.
 * 
 * @author plagge
 * 
 */
public class KodkodAnalysis extends DepthFirstAdapter {
	private static final Map<String, Formula> CONSTFORM = new HashMap<String, Formula>();
	private static final Map<String, ExprCompOperator> COMPOPS = new HashMap<String, ExprCompOperator>();
	private static final Map<String, FormulaOperator> BINFORMOPS = new HashMap<String, FormulaOperator>();
	private static final Map<String, Quantifier> QUANTIFIERS = new HashMap<String, Quantifier>();
	private static final Map<String, FormulaOperator> QUANTIFIER_FOP = new HashMap<String, FormulaOperator>();

	private static final Map<String, ExprOperator> BINEXPROPS = new HashMap<String, ExprOperator>();
	private static final Map<String, ExprOperator> MULTIEXPROPS = new HashMap<String, ExprOperator>();
	private static final Map<String, ExprOperator> UNEXPROPS = new HashMap<String, ExprOperator>();
	private static final Map<String, Multiplicity> MULTIPLICITIES = new HashMap<String, Multiplicity>();
	private static final Map<String, Expression> CONSTEXPR = new HashMap<String, Expression>();

	private static final Map<String, IntOperator> BININTEXPROPS = new HashMap<String, IntOperator>();
	private static final Map<String, IntCompOperator> BININTCOMPS = new HashMap<String, IntCompOperator>();
	private static final Map<String, IntCastOperator> INTCASTS = new HashMap<String, IntCastOperator>();

	{
		CONSTFORM.put(AFalseLogConst.class.getName(), Formula.FALSE);
		CONSTFORM.put(ATrueLogConst.class.getName(), Formula.TRUE);
		COMPOPS.put(AInLogopRel.class.getName(), ExprCompOperator.SUBSET);
		COMPOPS.put(AEqualsLogopRel.class.getName(), ExprCompOperator.EQUALS);
		BINFORMOPS.put(AOrLogopBinary.class.getName(), FormulaOperator.OR);
		BINFORMOPS.put(AImpliesLogopBinary.class.getName(),
				FormulaOperator.IMPLIES);
		BINFORMOPS.put(AIffLogopBinary.class.getName(), FormulaOperator.IFF);
		QUANTIFIERS.put(AAllQuantifier.class.getName(), Quantifier.ALL);
		QUANTIFIERS.put(AExistsQuantifier.class.getName(), Quantifier.SOME);
		QUANTIFIER_FOP.put(AAllQuantifier.class.getName(),
				FormulaOperator.IMPLIES);
		QUANTIFIER_FOP.put(AExistsQuantifier.class.getName(),
				FormulaOperator.AND);

		MULTIEXPROPS.put(AProductExprMultop.class.getName(),
				ExprOperator.PRODUCT);
		MULTIEXPROPS.put(AUnionExprMultop.class.getName(), ExprOperator.UNION);
		MULTIEXPROPS.put(AInterExprMultop.class.getName(),
				ExprOperator.INTERSECTION);
		BINEXPROPS.put(ADiffExprBinop.class.getName(), ExprOperator.DIFFERENCE);
		BINEXPROPS.put(AJoinExprBinop.class.getName(), ExprOperator.JOIN);
		BINEXPROPS.put(AOverwriteExprBinop.class.getName(),
				ExprOperator.OVERRIDE);
		UNEXPROPS.put(AClosureExprUnop.class.getName(), ExprOperator.CLOSURE);
		UNEXPROPS.put(AReflclsExprUnop.class.getName(),
				ExprOperator.REFLEXIVE_CLOSURE);
		UNEXPROPS.put(ATransposeExprUnop.class.getName(),
				ExprOperator.TRANSPOSE);
		MULTIPLICITIES
				.put(ALoneMultiplicity.class.getName(), Multiplicity.LONE);
		MULTIPLICITIES.put(ANoMultiplicity.class.getName(), Multiplicity.NO);
		MULTIPLICITIES.put(AOneMultiplicity.class.getName(), Multiplicity.ONE);
		MULTIPLICITIES.put(ASetMultiplicity.class.getName(), Multiplicity.SET);
		MULTIPLICITIES
				.put(ASomeMultiplicity.class.getName(), Multiplicity.SOME);
		CONSTEXPR.put(AEmptyExprConst.class.getName(), Expression.NONE);
		CONSTEXPR.put(AIdenExprConst.class.getName(), Expression.IDEN);
		CONSTEXPR.put(AUnivExprConst.class.getName(), Expression.UNIV);

		BININTEXPROPS.put(AAddIntexprBinop.class.getName(), IntOperator.PLUS);
		BININTEXPROPS.put(ASubIntexprBinop.class.getName(), IntOperator.MINUS);
		BININTEXPROPS.put(AMulIntexprBinop.class.getName(),
				IntOperator.MULTIPLY);

		BININTCOMPS.put(AEqualsIntCompOp.class.getName(), IntCompOperator.EQ);
		BININTCOMPS.put(AGreaterIntCompOp.class.getName(), IntCompOperator.GT);
		BININTCOMPS.put(AGreaterequalIntCompOp.class.getName(),
				IntCompOperator.GTE);
		BININTCOMPS.put(ALesserIntCompOp.class.getName(), IntCompOperator.LT);
		BININTCOMPS.put(ALesserequalIntCompOp.class.getName(),
				IntCompOperator.LTE);

		INTCASTS.put(APow2ExprCast.class.getName(), IntCastOperator.BITSETCAST);
		INTCASTS.put(AIntsetExprCast.class.getName(), IntCastOperator.INTCAST);
	}

	private final KodkodSession session;
	private final IPrologTermOutput pto;

	private Problem problem;

	private final Stack<Formula> formulaStack = new Stack<Formula>();
	private final Stack<Expression> expressionStack = new Stack<Expression>();
	private final Stack<IntExpression> intExpressionStack = new Stack<IntExpression>();
	private Map<String, Expression> variables = new HashMap<String, Expression>();

	public KodkodAnalysis(final KodkodSession session,
			final IPrologTermOutput pto) {
		this.session = session;
		this.pto = pto;
	}

	@Override
	public void inStart(final Start node) {
		problem = null;
		formulaStack.clear();
		expressionStack.clear();
		intExpressionStack.clear();
		variables.clear();
	}

	/**
	 * A problem description was entered. It will be added to the
	 * {@link KodkodSession}
	 */
	@Override
	public void caseAProblem(final AProblem node) {
		final String id = extractIdentifier(node.getId());
		problem = new Problem(id);

		registerTypes(node.getTypes());
		problem.createUniverse();
		addRelations(node.getRelations());

		node.getFormula().apply(this);
		problem.setFormula(formulaStack.pop());

		session.addProblem(problem.createImmutable());
	}

	private void registerTypes(final Collection<PType> types) {
		for (final PType ptype : types) {
			if (ptype instanceof AStandardType) {
				final AStandardType type = (AStandardType) ptype;
				final String id = extractIdentifier(type.getId());
				final int size = extractInt(type.getSize());
				problem.registerType(id, size);
			} else if (ptype instanceof AIntsType) {
				final AIntsType type = (AIntsType) ptype;
				addIntegerType(type);
			} else
				throw new IllegalStateException("unexpected type case "
						+ ptype.getClass().getName());
		}
	}

	/**
	 * A request has been entered. It will be added to the {@link KodkodSession}
	 * and the first solutions will be send directly.
	 */
	@Override
	public void caseARequest(final ARequest node) {
		final String problemId = extractIdentifier(node.getProblem());
		final ImmutableProblem problem = session.getProblem(problemId);
		if (problem != null) {
			final boolean signum = getSignum(node.getReqtype());
			int size = extractInt(node.getSize());
			final Map<String, TupleSet> args = extractArguments(
					node.getArguments(), problem);
			session.request(problem, signum, args);
			session.writeNextSolutions(problem, size, pto);
		} else {
			pto.openTerm("unknown").printAtom(problemId).closeTerm().fullstop();
		}
	}

	/**
	 * A command to send the next solutions of a request has been entered.
	 */
	@Override
	public void outAList(final AList node) {
		final String id = extractIdentifier(node.getProblem());
		final ImmutableProblem problem = session.getProblem(id);
		if (problem != null) {
			final int size = extractInt(node.getSize());
			session.writeNextSolutions(problem, size, pto);
		}
	}

	/**
	 * The stop command has been entered.
	 */
	@Override
	public void outAStop(final AStop node) {
		session.stop();
	}

	private Map<String, TupleSet> extractArguments(final List<PArgument> args,
			final ImmutableProblem problem) {
		final Map<String, TupleSet> result = new HashMap<String, TupleSet>();
		final Universe universe = problem.getUniverse();
		for (final PArgument parg : args) {
			final AArgument arg = (AArgument) parg;
			final String id = extractIdentifier(arg.getIdentifier());
			final RelationInfo info = problem.lookupRelationInfo(id);
			final TupleSet tupleSet = extractTuples(universe,
					info.getTupleType(), arg.getTuples());
			result.put(id, tupleSet);
		}
		return result;
	}

	@Override
	public void outAConstInnerformula(final AConstInnerformula node) {
		final String name = node.getLogConst().getClass().getName();
		final Formula formula = CONSTFORM.get(name);
		if (formula == null)
			throw new IllegalStateException("Unexpected constant " + name);
		formulaStack.push(formula);
	}

	@Override
	public void outAMultInnerformula(final AMultInnerformula node) {
		final String name = node.getMultiplicity().getClass().getName();
		final Multiplicity multiplicity = MULTIPLICITIES.get(name);
		if (multiplicity == null)
			throw new IllegalStateException("Unexpected multiplicity " + name);
		formulaStack.push(expressionStack.pop().apply(multiplicity));
	}

	@Override
	public void outARelInnerformula(final ARelInnerformula node) {
		final String name = node.getLogopRel().getClass().getName();
		final ExprCompOperator op = COMPOPS.get(name);
		if (op == null)
			throw new IllegalStateException("Unexpected relation operator "
					+ name);
		final Expression b = expressionStack.pop();
		final Expression a = expressionStack.pop();
		formulaStack.push(a.compare(op, b));
	}

	@Override
	public void outANotInnerformula(final ANotInnerformula node) {
		formulaStack.push(formulaStack.pop().not());
	}

	@Override
	public void caseAAndInnerformula(final AAndInnerformula node) {
		final Collection<PFormula> nodes = node.getFormula();
		final int size = nodes == null ? 0 : nodes.size();
		if (size == 0) {
			formulaStack.push(Formula.TRUE);
		} else if (size == 1) {
			// just put the inner formula onto the stack
			nodes.iterator().next().apply(this);
		} else {
			final Formula[] formulas = new Formula[size];
			int pos = 0;
			for (final PFormula sub : nodes) {
				sub.apply(this);
				formulas[pos] = formulaStack.pop();
				pos++;
			}
			formulaStack.push(Formula.and(formulas));
		}
	}

	@Override
	public void outABinaryInnerformula(final ABinaryInnerformula node) {
		final String name = node.getLogopBinary().getClass().getName();
		final FormulaOperator op = BINFORMOPS.get(name);
		if (op == null)
			throw new IllegalStateException("Unexpected operator " + name);
		final Formula b = formulaStack.pop();
		final Formula a = formulaStack.pop();
		formulaStack.push(a.compose(op, b));
	}

	@Override
	public void caseAQuantInnerformula(final AQuantInnerformula node) {
		final String name = node.getQuantifier().getClass().getName();
		final Quantifier quantifier = QUANTIFIERS.get(name);
		final FormulaOperator formulaOp = QUANTIFIER_FOP.get(name);
		if (quantifier == null || formulaOp == null)
			throw new IllegalStateException("Unexpected quantifier " + name);
		final Map<String, Expression> oldVars = variables;
		variables = new HashMap<String, Expression>(variables);
		final Declaration decls = extractDecls(node.getDecls());
		node.getFormula().apply(this);
		final Formula formula = decls.applyFormula(formulaStack.pop(),
				formulaOp);
		final Formula quantify = formula.quantify(quantifier,
				decls.getDeclarations());
		formulaStack.push(quantify);
		variables = oldVars;
	}

	@Override
	public void outAIntInnerformula(final AIntInnerformula node) {
		final String name = node.getIntCompOp().getClass().getName();
		final IntCompOperator op = BININTCOMPS.get(name);
		if (op == null)
			throw new IllegalStateException(
					"Unexpected integer comparision operator " + name);
		final IntExpression b = intExpressionStack.pop();
		final IntExpression a = intExpressionStack.pop();
		formulaStack.push(a.compare(op, b));
	}

	@Override
	public void outAFuncInnerformula(final AFuncInnerformula node) {
		final Expression range = expressionStack.pop();
		final Expression domain = expressionStack.pop();
		final Expression obj = expressionStack.pop();
		final PLogopFunction op = node.getLogopFunction();
		final Formula formula;
		if (obj instanceof Relation) {
			final Relation rel = (Relation) obj;
			if (op instanceof ATotalLogopFunction) {
				formula = rel.function(domain, range);
			} else if (op instanceof APartialLogopFunction) {
				formula = rel.partialFunction(domain, range);
			} else
				throw new IllegalStateException("unexpected function operator "
						+ op.getClass().getName());
		} else {
			final Multiplicity mult;
			if (op instanceof ATotalLogopFunction) {
				mult = Multiplicity.ONE;
			} else if (op instanceof APartialLogopFunction) {
				mult = Multiplicity.LONE;
			} else
				throw new IllegalStateException("unexpected function operator "
						+ op.getClass().getName());
			final Variable v = Variable.nary("__func", domain.arity());
			final Decl decl = v.declare(Multiplicity.ONE, domain);
			final Formula subset = obj.in(domain.product(range));
			final Formula unique = v.join(obj).apply(mult).forAll(decl);
			formula = subset.and(unique);
		}
		formulaStack.push(formula);
	}

	@Override
	public void caseAMultiInnerexpression(final AMultiInnerexpression node) {
		final String name = node.getExprMultop().getClass().getName();
		final ExprOperator op = MULTIEXPROPS.get(name);
		if (op == null)
			throw new IllegalStateException("Unexpected operator " + name);
		final Collection<PExpression> nodes = node.getExpressions();
		final int size = nodes == null ? 0 : nodes.size();
		if (size == 0)
			throw new IllegalStateException("missing argument for " + name);
		final Expression[] expressions = new Expression[size];
		int pos = 0;
		for (final PExpression sub : nodes) {
			sub.apply(this);
			expressions[pos] = expressionStack.pop();
			pos++;
		}
		expressionStack.push(Expression.compose(op, expressions));
	}

	@Override
	public void outABinaryInnerexpression(final ABinaryInnerexpression node) {
		final String name = node.getExprBinop().getClass().getName();
		final ExprOperator op = BINEXPROPS.get(name);
		if (op == null)
			throw new IllegalStateException("Unexpected operator " + name);
		final Expression b = expressionStack.pop();
		final Expression a = expressionStack.pop();
		expressionStack.push(a.compose(op, b));
	}

	@Override
	public void outAUnaryInnerexpression(final AUnaryInnerexpression node) {
		final String name = node.getExprUnop().getClass().getName();
		final ExprOperator op = UNEXPROPS.get(name);
		if (op == null)
			throw new IllegalStateException("Unexpected operator " + name);
		expressionStack.push(expressionStack.pop().apply(op));
	}

	@Override
	public void outARelrefInnerexpression(final ARelrefInnerexpression node) {
		final String id = extractIdentifier(node.getIdentifier());
		final Relation relation = problem.lookupRelation(id);
		expressionStack.push(relation);
	}

	@Override
	public void outAVarrefInnerexpression(final AVarrefInnerexpression node) {
		final String id = extractIdentifier(node.getIdentifier());
		final Expression var = variables.get(id);
		if (var == null)
			throw new IllegalStateException("unknown variable " + id);
		expressionStack.push(var);
	}

	@Override
	public void outAConstInnerexpression(final AConstInnerexpression node) {
		final String name = node.getExprConst().getClass().getName();
		final Expression expression = CONSTEXPR.get(name);
		if (expression == null)
			throw new IllegalStateException("Unexpected constant " + name);
		expressionStack.push(expression);
	}

	@Override
	public void outAConstInnerintexpression(final AConstInnerintexpression node) {
		final int value = extractInt(node.getZnumber());
		intExpressionStack.push(IntConstant.constant(value));
	}

	@Override
	public void outAPrjInnerexpression(final APrjInnerexpression node) {
		final int[] numbers = extractNumbers(node.getNumbers());
		final IntExpression[] prjs = new IntExpression[numbers.length];
		for (int i = 0; i < numbers.length; i++) {
			prjs[i] = IntConstant.constant(numbers[i]);
		}
		expressionStack.push(expressionStack.pop().project(prjs));
	}

	@Override
	public void outACastInnerexpression(final ACastInnerexpression node) {
		final IntExpression integer = intExpressionStack.pop();
		final String castName = node.getExprCast().getClass().getName();
		final IntCastOperator op = INTCASTS.get(castName);
		if (op == null)
			throw new IllegalStateException("Unexpected integer cast operator "
					+ castName);
		expressionStack.push(integer.cast(op));
	}

	@Override
	public void outAIfInnerexpression(final AIfInnerexpression node) {
		final Expression elseExpr = expressionStack.pop();
		final Expression thenExpr = expressionStack.pop();
		final Formula condition = formulaStack.pop();
		expressionStack.push(condition.thenElse(thenExpr, elseExpr));
	}

	@Override
	public void outACastInnerintexpression(final ACastInnerintexpression node) {
		final Expression expr = expressionStack.pop();
		intExpressionStack.push(expr.sum());
	}

	@Override
	public void outABinaryInnerintexpression(
			final ABinaryInnerintexpression node) {
		final String name = node.getIntexprBinop().getClass().getName();
		final IntOperator op = BININTEXPROPS.get(name);
		if (op == null)
			throw new IllegalStateException("Unexpected integer operator "
					+ name);
		final IntExpression b = intExpressionStack.pop();
		final IntExpression a = intExpressionStack.pop();
		intExpressionStack.push(a.compose(op, b));
	}

	@Override
	public void outACardInnerintexpression(final ACardInnerintexpression node) {
		intExpressionStack.push(expressionStack.pop().count());
	}

	@Override
	public void caseACompInnerexpression(final ACompInnerexpression node) {
		final Map<String, Expression> oldVars = variables;
		variables = new HashMap<String, Expression>(variables);
		final Declaration decls = extractDecls(node.getDecls());
		node.getFormula().apply(this);
		final Formula formula = decls.applyFormula(formulaStack.pop(),
				FormulaOperator.AND);
		expressionStack.push(formula.comprehension(decls.getDeclarations()));
		variables = oldVars;
	}

	private boolean getSignum(final PReqtype reqtype) {
		return reqtype instanceof APosReqtype;
	}

	private Declaration extractDecls(PDecls node) {
		final Declaration declaration = new Declaration();
		while (node instanceof AConsDecls) {
			final AConsDecls cons = (AConsDecls) node;
			final String id = extractIdentifier(cons.getId());
			final int arity = extractInt(cons.getArity());
			final String mname = cons.getMultiplicity().getClass().getName();
			cons.getExpression().apply(this);
			final Expression expression = expressionStack.pop();

			final Multiplicity multiplicity = MULTIPLICITIES.get(mname);
			if (multiplicity == null)
				throw new IllegalStateException("Unexpected multiplicity "
						+ mname);
			if (arity > 1 && Multiplicity.ONE.equals(multiplicity)) {
				createRelationDecl(id, arity, expression, declaration);
			} else {
				declare(id, arity, multiplicity, expression, declaration);
			}

			node = cons.getDecls();
		}
		if (declaration.isEmpty())
			throw new IllegalStateException(
					"no declarations in quantified formula");
		return declaration;
	}

	private void createRelationDecl(final String id, final int arity,
			Expression expression, final Declaration declaration) {
		Expression substitution = null;
		for (int p = 0; p < arity; p++) {
			final String tmpId = id + "_#_" + p;
			final Expression prj = expression.project(IntConstant.constant(p));
			final Decl tmpDecl = declare(tmpId, 1, Multiplicity.ONE, prj,
					declaration);
			final Variable tmpVar = tmpDecl.variable();
			if (substitution == null) {
				substitution = tmpVar;
			} else {
				substitution = substitution.product(tmpVar);
			}
		}
		variables.put(id, substitution);
		declaration.addFormula(substitution.in(expression));
	}

	private Decl declare(final String id, final int arity,
			final Multiplicity multiplicity, final Expression expression,
			final Declaration declaration) {
		final Variable variable = Variable.nary(id, arity);
		variables.put(id, variable);
		final Decl decl = variable.declare(multiplicity, expression);
		declaration.addDeclaration(decl);
		return decl;
	}

	private void addRelations(final Collection<PRelation> nodes) {
		for (final PRelation node : nodes) {
			final ARelation rel = (ARelation) node;
			final String id = extractIdentifier(rel.getId());
			final boolean isExact = isExactRelation(rel.getExtsub());
			final boolean isSingleton = rel.getSingleton() != null;
			final Type[] types = extractTypes(rel.getTypes());
			checkSingletonForTypes(id, isSingleton, types);
			final Universe universe = problem.getUniverse();
			final PTupleset tupleset = rel.getElements();
			final TupleType tupleType = new TupleType(types, isSingleton);
			final TupleSet ptset = extractTupleSet(universe, tupleset,
					tupleType);
			problem.addRelation(id, isExact, tupleType, ptset);
		}
	}

	private void checkSingletonForTypes(final String id,
			final boolean isSingleton, final Type[] types) {
		if (!isSingleton) {
			for (final Type type : types) {
				if (type.oneValueNeedsCompleteTupleSet())
					throw new IllegalArgumentException("Relation " + id
							+ " makes use of type " + type
							+ " but is not declared as singleton");
			}
		}
	}

	private boolean isExactRelation(final PReltype extsub) {
		final boolean result;
		if (extsub instanceof AExactReltype) {
			result = true;
		} else if (extsub instanceof ASubsetReltype) {
			result = false;
		} else
			throw new IllegalStateException(
					"unexpected relation type (exact/sub) "
							+ extsub.getClass().getName());
		return result;
	}

	private void addIntegerType(final AIntsType relnode) {
		final APowpart pow2spec = (APowpart) relnode.getPow2();
		final ABitpart bitsspec = (ABitpart) relnode.getIntatoms();
		final String pow2Name = extractIdentifier(pow2spec.getId());
		final IntegerIntervall pow2Interval = new IntegerIntervall(
				extractInt(pow2spec.getLower()),
				extractInt(pow2spec.getUpper()));
		final String bitsName;
		final IntegerIntervall intsetInterval;

		if (bitsspec != null) {
			bitsName = extractIdentifier(bitsspec.getId());
			final int lower = extractInt(bitsspec.getLower());
			final int upper = extractInt(bitsspec.getUpper());
			intsetInterval = new IntegerIntervall(lower, upper);
		} else {
			bitsName = null;
			intsetInterval = null;
		}

		problem.registerIntegerTypes(pow2Name, bitsName, intsetInterval,
				pow2Interval);
	}

	private TupleSet extractTupleSet(final Universe universe,
			final PTupleset node, final TupleType tupleType) {
		final TupleSet result;
		if (node == null) {
			result = tupleType.createAllTuples(universe);
		} else {
			ATupleset aTupleset = (ATupleset) node;
			result = extractTuples(universe, tupleType, aTupleset.getTuples());
		}
		return result;
	}

	private Type lookupTypeInterval(final TIdentifier tid) {
		final String id = extractIdentifier(tid);
		final Type type = problem.lookupType(id);
		if (type == null)
			throw new IllegalArgumentException("unknown type " + id);
		return type;
	}

	private TupleSet extractTuples(final Universe universe,
			final TupleType tupleType, final Collection<PTuple> ptuples) {
		if (tupleType.isSingleton()) {
			if (ptuples.size() != 1)
				throw new IllegalArgumentException(
						"singleton type expects exactly one element, but there were "
								+ ptuples.size());
		}
		final int arity = tupleType.getArity();
		final Collection<int[]> tuples = new ArrayList<int[]>();
		for (final PTuple pTuple : ptuples) {
			final ATuple aTuple = (ATuple) pTuple;
			final int[] numbers = extractNumbers(aTuple.getNumbers());
			if (numbers.length != arity)
				throw new IllegalArgumentException("expected " + arity
						+ "-tuple, but is a " + numbers.length + "-tuple");
			tuples.add(numbers);
		}
		return tupleType.createTupleSet(universe, tuples);
	}

	private int[] extractNumbers(final Collection<TNumber> nodes) {
		final int[] result = new int[nodes.size()];
		int i = 0;
		for (final TNumber node : nodes) {
			result[i] = extractInt(node);
			i++;
		}
		return result;
	}

	private static int extractInt(final TNumber node) {
		return Integer.parseInt(node.getText());
	}

	private static int extractInt(final PZnumber node) {
		final int factor;
		final TNumber numberNode;
		if (node instanceof APosZnumber) {
			factor = 1;
			numberNode = ((APosZnumber) node).getNumber();
		} else if (node instanceof ANegZnumber) {
			factor = -1;
			numberNode = ((ANegZnumber) node).getNumber();
		} else
			throw new IllegalStateException("Unexpected class "
					+ node.getClass().getName());
		return factor * extractInt(numberNode);
	}

	private Type[] extractTypes(final Collection<TIdentifier> nodes) {
		final Type[] result = new Type[nodes.size()];
		int i = 0;
		for (final TIdentifier node : nodes) {
			result[i] = lookupTypeInterval(node);
			i++;
		}
		return result;
	}

	private static String extractIdentifier(final TIdentifier node) {
		return node == null ? null : node.getText();
	}

	private static final class Declaration {
		private Decls declarations;
		private Formula formula;

		public void addDeclaration(final Decls decls) {
			if (declarations == null) {
				declarations = decls;
			} else {
				declarations = declarations.and(decls);
			}
		}

		public boolean isEmpty() {
			return declarations == null;
		}

		public void addFormula(final Formula form) {
			if (formula == null) {
				formula = form;
			} else {
				formula = formula.and(form);
			}
		}

		public Decls getDeclarations() {
			return declarations;
		}

		public Formula applyFormula(Formula orig, FormulaOperator op) {
			return formula == null ? orig : formula.compose(op, orig);
		}
	}
}
