package net.assembler.sixteenhigh.tokeniser.statment.expression;

public class Precedence
{
//  maiPrecedence.Init();
//	maiPrecedence.Add(0);  // CO_Increment,
//	maiPrecedence.Add(0);  // CO_Decrement,
//	maiPrecedence.Add(6);  // CO_EqualTo,
//	maiPrecedence.Add(6);  // CO_NotEqualTo,
//	maiPrecedence.Add(5);  // CO_GreaterThanEqualTo,
//	maiPrecedence.Add(5);  // CO_LessThanEqualTo,
//	maiPrecedence.Add(11);  // CO_LogicalOr,
//	maiPrecedence.Add(10);  // CO_LogicalAnd,
//	maiPrecedence.Add(4);  // CO_LeftShift,
//	maiPrecedence.Add(4);  // CO_RightShift,
//	maiPrecedence.Add(3);  // CO_Add,
//	maiPrecedence.Add(3);  // CO_Subtract,
//	maiPrecedence.Add(2);  // CO_Multiply,
//	maiPrecedence.Add(2);  // CO_Divide,
//	maiPrecedence.Add(2);  // CO_Modulus,
//	maiPrecedence.Add(1);  // CO_LogicalNot,
//	maiPrecedence.Add(7);  // CO_BitwiseAnd,
//	maiPrecedence.Add(9);  // CO_BitwiseOr,
//	maiPrecedence.Add(8);  // CO_BitwiseXor,
//	maiPrecedence.Add(5);  // CO_LessThan,
//	maiPrecedence.Add(5);  // CO_GreaterThan,
//	maiPrecedence.Add(1);  // CO_BitwiseNot,
//
//  //////////////////////////////////////////////////////////////////////////
////
////
////////////////////////////////////////////////////////////////////////////
//  bool CCalculator::
//
//  BuildExpression(CCalcExpression**ppcExpression, CArrayIntAndPointer*pcArray)
//  {
//    int iIndex;
//    CCalcOperator * pcOperator;
//    CCalcExpression * pcOperand;
//    CCalcObject * pcObject;
//    CCalcObject * pcObjectLeft;
//    CCalcObject * pcObjectRight;
//    CCalcUnaryExpression * pcUnary;
//    CCalcBinaryExpression * pcBinary;
//    CCalcExpression * pcOperandLeft;
//    CCalcExpression * pcOperandRight;
//    int iOldUsedElements;
//    CChars szStart;
//    bool bUnary;
//
//    szStart.Init();
//    Print( & szStart, pcArray);
//
//    iOldUsedElements = pcArray -> NumElements();
//    while (pcArray -> NumElements() > 1)
//    {
//      if (pcArray -> NumElements() > iOldUsedElements)
//      {
//        return SetError( & szStart,
//        pcArray, ppcExpression, "Number of elements in expression INCREASED from [", "] to [", "] which should not be possible.")
//        ;
//      }
//      iOldUsedElements = pcArray -> NumElements();
//
//      iIndex = GetMinPrecedence(pcArray);
//      if (iIndex == -1)
//      {
//        return SetError( & szStart,
//        pcArray, ppcExpression, "Confused trying to find order of precedence for inital [", "] with current [", "].");
//      }
//      pcOperator = (CCalcOperator *) pcArray -> GetPtr(iIndex);
//
//      if (pcOperator -> IsAmbiguous())
//      {
//        pcObject = (CCalcObject *) pcArray -> SafeGetPtr(iIndex - 1);
//        bUnary = false;
//        if (pcObject == NULL)
//        {
//          bUnary = true;
//        }
//        else
//        {
//          if (pcObject -> IsOperator())
//          {
//            bUnary = true;
//          }
//        }
//        ResolveAmbiguity(pcOperator, bUnary);
//      }
//
//      if (pcOperator -> IsUnary())
//      {
//        //For the time being always assume Right-to-Left associativity.
//        pcObject = (CCalcObject *) pcArray -> SafeGetPtr(iIndex + 1);
//        if (pcObject)
//        {
//          if (pcObject -> IsExpression())
//          {
//            pcOperand = (CCalcExpression *) pcObject;
//            pcUnary = NewMalloc < CCalcUnaryExpression > ();
//            pcUnary -> Set(pcOperand, pcOperator);
//            pcArray -> RemoveAt(iIndex + 1);
//            pcArray -> Set(iIndex, pcUnary, 0);
//          }
//          else
//          {
//            return SetError( & szStart,
//            pcArray, ppcExpression, "Unary operator only works on expressions given inital [", "] and current [", "].");
//          }
//        }
//        else
//        {
//          return SetError( & szStart,
//          pcArray, ppcExpression, "Unary operator needs right hand operand for inital [", "] with current [", "].");
//        }
//      }
//      else if (pcOperator -> IsBinary())
//      {
//        pcObjectLeft = (CCalcObject *) pcArray -> SafeGetPtr(iIndex - 1);
//        if (pcObjectLeft)
//        {
//          pcObjectRight = (CCalcObject *) pcArray -> SafeGetPtr(iIndex + 1);
//          if (pcObjectRight)
//          {
//            if (pcObjectLeft -> IsExpression() && pcObjectRight -> IsExpression())
//            {
//              pcOperandLeft = (CCalcExpression *) pcObjectLeft;
//              pcOperandRight = (CCalcExpression *) pcObjectRight;
//              pcBinary = NewMalloc < CCalcBinaryExpression > ();
//              pcBinary -> Set(pcOperandLeft, pcOperator, pcOperandRight);
//              pcArray -> RemoveAt(iIndex + 1);
//              pcArray -> Set(iIndex, pcBinary, 0);
//              pcArray -> RemoveAt(iIndex - 1);
//            }
//            else
//            {
//              return SetError( & szStart,
//              pcArray, ppcExpression, "Binary operator only works on expressions given inital [", "] and current [", "].")
//              ;
//            }
//
//          }
//          else
//          {
//            return SetError( & szStart,
//            pcArray, ppcExpression, "Binary operator needs right hand operand for inital [", "] with current [", "].");
//          }
//        }
//        else
//        {
//          return SetError( & szStart,
//          pcArray, ppcExpression, "Binary operator needs left hand operand for inital [", "] with current [", "].");
//        }
//      }
//      else
//      {
//        return SetError( & szStart,
//        pcArray, ppcExpression, "Don't know what style of operator this is [", "] with current [", "].");
//      }
//    }
//	*ppcExpression = (CCalcExpression *) pcArray -> GetPtr(0);
//    szStart.Kill();
//    return true;
//  }
//
//
////////////////////////////////////////////////////////////////////////////
////
////
////////////////////////////////////////////////////////////////////////////
//void CCalculator::ResolveAmbiguity(CCalcOperator* pcOperator, bool bIsUnary)
//{
//	if (bIsUnary)
//	{
//		if (pcOperator->meOp == CO_Add)
//		{
//			pcOperator->meOp = CO_UnaryAdd;
//		}
//		else if (pcOperator->meOp == CO_Subtract)
//		{
//			pcOperator->meOp = CO_UnarySubtract;
//		}
//	}
//	else
//	{
//		if (pcOperator->meOp == CO_UnaryAdd)
//		{
//			pcOperator->meOp = CO_Add;
//		}
//		else if (pcOperator->meOp == CO_UnarySubtract)
//		{
//			pcOperator->meOp = CO_Subtract;
//		}
//	}
//}
}
