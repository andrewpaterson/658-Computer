package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.types.PrimitiveDefinition;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TypeModifierMap
{
  protected Map<Integer, List<TypeModifier>> pointerTypeModifierMap;
  protected Map<PrimitiveTypeCode, Map<TypeModifier, TypeDefinition>> typeCodeMap;

  public TypeModifierMap()
  {
    typeCodeMap = new LinkedHashMap<>();

    pointerTypeModifierMap = new LinkedHashMap<>();
    for (int i = 0; i < 3; i++)
    {
      pointerTypeModifierMap.put(i, new ArrayList<>());
    }
  }

  public PrimitiveDefinition addPrimitiveDefinition(PrimitiveTypeCode primitiveTypeCode, List<Long> arrayMatrix, int pointerCount)
  {
    Map<TypeModifier, TypeDefinition> typeModifierMap = typeCodeMap.get(primitiveTypeCode);
    if (typeModifierMap == null)
    {
      typeModifierMap = new LinkedHashMap<>();
      typeCodeMap.put(primitiveTypeCode, typeModifierMap);
    }

    TypeModifier typeModifier = addTypeModifier(arrayMatrix, pointerCount);

    PrimitiveDefinition typeDefinition = (PrimitiveDefinition) typeModifierMap.get(typeModifier);
    if (typeDefinition == null)
    {
      typeDefinition = new PrimitiveDefinition(primitiveTypeCode, arrayMatrix, pointerCount);
      typeModifierMap.put(typeModifier, typeDefinition);
    }
    return typeDefinition;
  }

  public TypeModifier addTypeModifier(List<Long> arrayMatrix, int pointerCount)
  {
    List<TypeModifier> typeModifiers = pointerTypeModifierMap.get(pointerCount);
    if (typeModifiers == null)
    {
      typeModifiers = new ArrayList<>();
      pointerTypeModifierMap.put(pointerCount, typeModifiers);
    }

    TypeModifier typeModifier = getTypeModifier(typeModifiers, arrayMatrix);
    if (typeModifier == null)
    {
      typeModifier = new TypeModifier(pointerCount, arrayMatrix);
      typeModifiers.add(typeModifier);
    }

    return typeModifier;
  }

  private TypeModifier getTypeModifier(List<TypeModifier> typeModifiers, List<Long> arrayMatrix)
  {
    for (TypeModifier typeModifier : typeModifiers)
    {
      if (typeModifier.matches(arrayMatrix))
      {
        return typeModifier;
      }
    }
    return null;
  }
}

