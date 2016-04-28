// Generated from Shopping.g4 by ANTLR 4.5.3
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ShoppingParser}.
 */
public interface ShoppingListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ShoppingParser#item}.
	 * @param ctx the parse tree
	 */
	void enterItem(ShoppingParser.ItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShoppingParser#item}.
	 * @param ctx the parse tree
	 */
	void exitItem(ShoppingParser.ItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShoppingParser#ware}.
	 * @param ctx the parse tree
	 */
	void enterWare(ShoppingParser.WareContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShoppingParser#ware}.
	 * @param ctx the parse tree
	 */
	void exitWare(ShoppingParser.WareContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShoppingParser#amount}.
	 * @param ctx the parse tree
	 */
	void enterAmount(ShoppingParser.AmountContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShoppingParser#amount}.
	 * @param ctx the parse tree
	 */
	void exitAmount(ShoppingParser.AmountContext ctx);
	/**
	 * Enter a parse tree produced by {@link ShoppingParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(ShoppingParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ShoppingParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(ShoppingParser.UnitContext ctx);
}