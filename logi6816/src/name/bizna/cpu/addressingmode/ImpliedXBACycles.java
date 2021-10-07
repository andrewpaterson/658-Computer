package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Implied;

public class ImpliedXBACycles
    extends InstructionCycles
{
  //19b
  public ImpliedXBACycles(Consumer<Cpu65816> consumer)
  {
    super(Implied,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), E(consumer), DONE()));
  }
}

