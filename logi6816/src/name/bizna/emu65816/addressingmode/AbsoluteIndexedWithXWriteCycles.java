package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXWriteCycles
    extends InstructionCycles
{
  //6a
  public AbsoluteIndexedWithXWriteCycles(Consumer<Cpu65816> operation)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), IO(), new NoteFourX(false)),
          new BusCycle(Address(DBR(), AA(), X()), E(operation), Write_DataLow(), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Write_DataHigh(), DONE16Bit(Width.A)));
  }
}
