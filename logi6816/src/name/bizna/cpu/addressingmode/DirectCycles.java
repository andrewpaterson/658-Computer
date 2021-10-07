package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Direct;

public class DirectCycles
    extends InstructionCycles
{
  //10a
  public DirectCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_DataLow(), E8Bit(operation), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(width)));
  }
}

