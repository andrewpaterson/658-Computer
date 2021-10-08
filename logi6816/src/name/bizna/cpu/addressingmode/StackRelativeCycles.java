package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.StackRelative;
import static name.bizna.cpu.WidthFromRegister.A;

public class StackRelativeCycles
    extends InstructionCycles
{
  public StackRelativeCycles(Consumer<Cpu65816> operation)
  {
    //23
    super(StackRelative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), D0(), o(1)), SP_inc(), Read_DataLow(), E8Bit(operation), DONE8Bit(A)),
          new BusCycle(Address(S(), D0(), o(1)), SP_inc(), Read_DataHigh(), E16Bit(operation), DONE16Bit(A)));
  }
}

