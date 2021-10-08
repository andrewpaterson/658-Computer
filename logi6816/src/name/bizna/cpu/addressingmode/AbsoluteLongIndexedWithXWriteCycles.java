package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.AbsoluteLongIndexedWithX;
import static name.bizna.cpu.WidthFromRegister.A;

public class AbsoluteLongIndexedWithXWriteCycles
    extends InstructionCycles
{
  //5
  public AbsoluteLongIndexedWithXWriteCycles(Consumer<Cpu65816> operation)
  {
    super(AbsoluteLongIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA(), X()), E(operation), Write_DataLow(), DONE8Bit(A)),
          new BusCycle(Address(AAB(), AA(), X(), o(1)), Write_DataHigh(), DONE16Bit(A)));
  }
}

