package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class VariableTokenExpression
    implements TokenExpression
{
  protected int dereferenceCount;
  protected boolean reference;
  protected List<VariableMember> members;

  public VariableTokenExpression(VariableMember variableMember, int dereferenceCount, boolean reference)
  {
    this.dereferenceCount = dereferenceCount;
    this.reference = reference;
    this.members = new ArrayList<>();
    this.members.add(variableMember);
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    String reference = this.reference ? "&" : "";
    StringBuilder asterisks = StringUtil.pad("*", dereferenceCount);
    StringBuilder memberBuilder = new StringBuilder();
    boolean first = true;
    for (VariableMember member : members)
    {
      if (first)
      {
        first = false;
      }
      else
      {
        memberBuilder.append('.');
      }

      memberBuilder.append(member.print(sixteenHighKeywords));
    }
    return asterisks + reference + memberBuilder.toString();
  }

  @Override
  public boolean isVariable()
  {
    return true;
  }

  public void addMember(VariableMember member)
  {
    members.add(member);
  }

  public int getDereferenceCount()
  {
    return dereferenceCount;
  }

  public boolean isReference()
  {
    return reference;
  }

  public List<VariableMember> getMembers()
  {
    return members;
  }

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }
}

