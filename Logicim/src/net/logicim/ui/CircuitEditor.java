package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.circuit.CircuitData;
import net.logicim.data.circuit.TimelineData;
import net.logicim.data.integratedcircuit.common.DiscreteData;
import net.logicim.data.trace.TraceData;
import net.logicim.data.trace.TraceLoader;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.domain.common.voltage.VoltageRepresentation;
import net.logicim.domain.power.PowerSource;
import net.logicim.ui.common.*;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.integratedcircuit.standard.power.PowerSourceView;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected Set<DiscreteView> discreteViews;
  protected Set<TraceView> traceViews;
  protected Circuit circuit;
  protected Simulation simulation;
  protected VoltageRepresentation colours;

  public CircuitEditor(VoltageRepresentation colours)
  {
    this.circuit = new Circuit();
    this.simulation = circuit.resetSimulation();
    this.discreteViews = new LinkedHashSet<>();
    this.traceViews = new LinkedHashSet<>();
    this.colours = colours;
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    long time = getTime();
    for (TraceView traceView : traceViews)
    {
      traceView.paint(graphics, viewport, time);
    }

    for (DiscreteView discreteView : discreteViews)
    {
      discreteView.paint(graphics, viewport, time);
    }
  }

  protected long getTime()
  {
    return simulation.getTime();
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void add(DiscreteView discreteView)
  {
    discreteViews.add(discreteView);
  }

  public void add(TraceView view)
  {
    traceViews.add(view);
  }

  public Set<PortView> deleteDiscreteView(DiscreteView discreteView)
  {
    if (discreteView instanceof IntegratedCircuitView)
    {
      return deleteIntegratedCircuit((IntegratedCircuitView<?, ?>) discreteView);
    }
    else if (discreteView instanceof PowerSourceView)
    {
      return deletePowerSource((PowerSourceView) discreteView);
    }
    else if (discreteView == null)
    {
      throw new SimulatorException("Cannot delete null view.");
    }
    else
    {
      throw new SimulatorException("Cannot delete view of class [%s].", discreteView.getClass().getSimpleName());
    }
  }

  protected Set<PortView> deleteIntegratedCircuit(IntegratedCircuitView<?, ?> integratedCircuitView)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    List<PortView> portViews = integratedCircuitView.getPorts();
    for (PortView portView : portViews)
    {
      ConnectionView connection = portView.getConnection();
      if (connection != null)
      {
        disconnectTraceNet(findConnections(connection));

        connection.remove(integratedCircuitView);
        connectionViews.add(connection);
      }
    }

    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    circuit.remove(integratedCircuit, simulation);
    discreteViews.remove(integratedCircuitView);

    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      updatedPortViews.addAll(connectConnections(connectionView));
    }

    return updatedPortViews;
  }

  protected Set<PortView> deletePowerSource(PowerSourceView powerSourceView)
  {
    List<ConnectionView> connectionViews = new ArrayList<>();
    List<PortView> portViews = powerSourceView.getPorts();
    for (PortView portView : portViews)
    {
      ConnectionView connection = portView.getConnection();
      if (connection != null)
      {
        disconnectTraceNet(findConnections(connection));

        connection.remove(powerSourceView);
        connectionViews.add(connection);
      }
    }

    PowerSource powerSource = powerSourceView.getPowerSource();
    circuit.remove(powerSource, simulation);
    discreteViews.remove(powerSourceView);

    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    for (ConnectionView connectionView : connectionViews)
    {
      updatedPortViews.addAll(connectConnections(connectionView));
    }

    return updatedPortViews;
  }

  public void remove(TraceView traceView)
  {
    ConnectionView startConnection = traceView.getStartConnection();
    ConnectionView endConnection = traceView.getEndConnection();

    disconnectTraceNet(findConnections(startConnection));
    disconnectTraceNet(findConnections(endConnection));

    startConnection.remove(traceView);
    endConnection.remove(traceView);
    traceView.removed();
    traceViews.remove(traceView);
  }

  public void disconnectTraceNet(Set<ConnectionView> connectionsNet)
  {
    for (ConnectionView connection : connectionsNet)
    {
      List<ComponentView> connectedComponents = connection.getConnectedComponents();
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          ((TraceView) connectedComponent).disconnectTraceNet();
        }
        else if (connectedComponent instanceof IntegratedCircuitView)
        {
          Int2D position = connection.getGridPosition();
          if (position != null)
          {
            PortView portView = ((IntegratedCircuitView<?, ?>) connectedComponent).getPortInGrid(position);
            portView.disconnectTraceNet(simulation);
          }
        }
      }
    }
  }

  public Set<PortView> connectConnections(ConnectionView connectionView)
  {
    Set<ConnectionView> connectionsNet = findConnections(connectionView);
    return connectToTraceNet(connectionsNet, new TraceNet());
  }

  public Simulation reset()
  {
    simulation = circuit.resetSimulation();
    return simulation;
  }

  public void runSimultaneous()
  {
    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    simulation.runToTime(timeForward);
  }

  public DiscreteView getDiscreteViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<DiscreteView> selectedViews = getDiscreteViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      DiscreteView closestView = null;
      for (DiscreteView view : selectedViews)
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
        float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<DiscreteView> getDiscreteViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    Int2D boundBoxPosition = new Int2D();
    Int2D boundBoxDimension = new Int2D();
    List<DiscreteView> selectedViews = new ArrayList<>();
    for (DiscreteView view : discreteViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(screenPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<DiscreteView> getDiscreteViewsInGridSpace(Int2D gridPosition)
  {
    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<DiscreteView> selectedViews = new ArrayList<>();
    for (DiscreteView view : discreteViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(gridPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<TraceView> getTraceViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    int x = viewport.transformScreenToGridX(screenPosition.x);
    int y = viewport.transformScreenToGridY(screenPosition.y);
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : this.traceViews)
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public TraceView getTraceViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<TraceView> selectedViews = getTraceViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D center = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      TraceView closestView = null;
      for (TraceView view : selectedViews)
      {
        view.getCenter(center);
        float distance = BoundingBox.calculateDistance(screenPosition, center);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<TraceView> getTraceViewsInGridSpace(Int2D position)
  {
    return getTraceViewsInGridSpace(position.x, position.y);
  }

  public List<TraceView> getTraceViewsInGridSpace(int x, int y)
  {
    List<TraceView> result = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      if (traceView.contains(x, y))
      {
        result.add(traceView);
      }
    }
    return result;
  }

  public List<TraceOverlap> getTracesOverlapping(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      LineOverlap overlap = traceView.getOverlap(line);
      if (overlap != null)
      {
        overlaps.add(new TraceOverlap(overlap, traceView));
      }
    }
    return overlaps;
  }

  public List<ComponentView> getComponents(Int2D position)
  {
    ConnectionView connectionView = getConnection(position);
    return connectionView.getConnectedComponents();
  }

  public ConnectionView getConnection(Int2D position)
  {
    List<DiscreteView> discreteViews = getDiscreteViewsInGridSpace(position);
    List<TraceView> traceViews = getTraceViewsInGridSpace(position);
    Set<ConnectionView> connectionViews = new HashSet<>();

    for (TraceView traceView : traceViews)
    {
      ConnectionView connectionView = traceView.getConnectionsInGrid(position);
      if (connectionView != null)
      {
        connectionViews.add(connectionView);
      }
    }

    for (DiscreteView discreteView : discreteViews)
    {
      PortView portView = discreteView.getPortInGrid(position.x, position.y);
      if (portView != null)
      {
        connectionViews.add(portView.getConnection());
      }
    }

    if (connectionViews.size() == 1)
    {
      return connectionViews.iterator().next();
    }
    else if (connectionViews.size() == 0)
    {
      return null;
    }
    else
    {
      throw new SimulatorException("Expected all connections at location [" + position.toString() + "] to be the same.");
    }
  }

  public ConnectionView getOrAddConnection(Int2D position, ComponentView componentView)
  {
    ConnectionView connection = getConnection(position);
    if (connection != null)
    {
      connection.add(componentView);
      return connection;
    }
    else
    {
      return new ConnectionView(componentView);
    }
  }

  public Set<ConnectionView> findConnections(ConnectionView connection)
  {
    if (connection != null)
    {
      ArrayList<ConnectionView> connectionsToProcess = new ArrayList<>();
      connectionsToProcess.add(connection);
      return findConnections(connectionsToProcess);
    }
    else
    {
      return new LinkedHashSet<>();
    }
  }

  public Set<ConnectionView> findConnections(List<ConnectionView> connectionsToProcess)
  {
    Set<ConnectionView> connectionsNet = new LinkedHashSet<>();
    while (connectionsToProcess.size() > 0)
    {
      ConnectionView currentConnection = connectionsToProcess.get(0);
      connectionsToProcess.remove(0);
      connectionsNet.add(currentConnection);

      List<ComponentView> components = currentConnection.getConnectedComponents();
      if (components != null)
      {
        for (ComponentView component : components)
        {
          if (component instanceof TraceView)
          {
            TraceView traceView = (TraceView) component;
            ConnectionView opposite = traceView.getOpposite(currentConnection);
            if (opposite != null)
            {
              if (!connectionsNet.contains(opposite))
              {
                connectionsToProcess.add(opposite);
              }
            }
          }
        }
      }
    }
    return connectionsNet;
  }

  public Set<PortView> connectToTraceNet(Set<ConnectionView> connectionsNet, TraceNet trace)
  {
    Set<PortView> connectedPorts = new LinkedHashSet<>();
    for (ConnectionView connection : connectionsNet)
    {
      List<ComponentView> connectedComponents = connection.getConnectedComponents();
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          ((TraceView) connectedComponent).connectTraceNet(trace, simulation);
        }
        else if (connectedComponent instanceof DiscreteView)
        {
          Int2D position = connection.getGridPosition();
          if (position != null)
          {
            PortView portView = connectedComponent.getPortInGrid(position);
            portView.connectTraceNet(trace, simulation);
            connectedPorts.add(portView);
          }
        }
      }
    }
    return connectedPorts;
  }

  public TraceView mergeTrace(TraceView traceView)
  {
    Rotation direction = traceView.getDirection();
    ConnectionView startConnection = traceView.getStartConnection();
    ConnectionView endConnection = traceView.getEndConnection();

    List<TraceView> towardsEnd = findStraightTracesInDirection(traceView, direction, endConnection);
    List<TraceView> towardsStart = findStraightTracesInDirection(traceView, direction.opposite(), startConnection);
    if (towardsEnd.size() > 0 | (towardsStart.size() > 0))
    {
      List<TraceView> mergeViews = new ArrayList<>(towardsStart);
      mergeViews.add(traceView);
      mergeViews.addAll(towardsEnd);
      Int2D smallest = new Int2D(traceView.getMinimumX(), traceView.getMinimumY());
      Int2D largest = new Int2D(traceView.getMaximumX(), traceView.getMaximumY());
      for (TraceView mergeView : mergeViews)
      {
        int x1 = mergeView.getMinimumX();
        if (x1 < smallest.x)
        {
          smallest.x = x1;
        }
        int y1 = mergeView.getMinimumY();
        if (y1 < smallest.y)
        {
          smallest.y = y1;
        }
        int x2 = mergeView.getMaximumX();
        if (x2 > largest.x)
        {
          largest.x = x2;
        }
        int y2 = mergeView.getMaximumY();
        if (y2 > largest.y)
        {
          largest.y = y2;
        }
        remove(mergeView);
      }

      if (isValidTrace(smallest, largest))
      {
        return new TraceView(this, smallest, largest);
      }
      else
      {
        throw new SimulatorException("Invalid trace created from merged traces.");
      }
    }
    else
    {
      return traceView;
    }
  }

  private boolean isValidTrace(Int2D start, Int2D end)
  {
    if ((start != null) && (end != null))
    {
      if (((start.x == end.x) || (start.y == end.y)) &&
          !((start.x == end.x) && (start.y == end.y)))
      {
        return true;
      }
    }
    return false;
  }

  private List<TraceView> findStraightTracesInDirection(TraceView traceView, Rotation direction, ConnectionView endConnection)
  {
    ArrayList<TraceView> result = new ArrayList<>();

    TraceView currentTrace = traceView;
    ConnectionView currentConnection = endConnection;
    for (; ; )
    {
      List<ComponentView> connectedComponents = currentConnection.getConnectedComponents();
      TraceView otherTrace = currentTrace.getOtherTrace(connectedComponents);

      if (otherTrace != null)
      {
        Rotation otherDirection = otherTrace.getDirection();
        if (direction.isStraight(otherDirection))
        {
          result.add(otherTrace);
          currentConnection = otherTrace.getOpposite(currentConnection);
          currentTrace = otherTrace;
        }
        else
        {
          break;
        }
      }
      else
      {
        break;
      }
    }

    return result;
  }

  private void splitTrace(Int2D position, Rotation direction)
  {
    List<TraceView> traceViews = getTraceViewsInGridSpace(position);
    for (TraceView traceView : traceViews)
    {
      ConnectionView connectionView = traceView.getPotentialConnectionsInGrid(position);
      if (connectionView instanceof HoverConnectionView)
      {
        if (!direction.isStraight(traceView.getDirection()))
        {
          Int2D startPosition = traceView.getStartPosition();
          Int2D endPosition = traceView.getEndPosition();
          remove(traceView);

          new TraceView(this, startPosition, position);
          new TraceView(this, position, endPosition);
        }
      }
    }
  }

  public Set<PortView> deleteTrace(TraceView traceView)
  {
    if (!traceView.isRemoved())
    {
      ConnectionView startConnection = traceView.getStartConnection();
      ConnectionView endConnection = traceView.getEndConnection();

      remove(traceView);

      Set<PortView> portViews = mergeAndConnect(startConnection);
      portViews.addAll(mergeAndConnect(endConnection));
      return portViews;
    }
    return new LinkedHashSet<>();
  }

  protected Set<PortView> mergeAndConnect(ConnectionView startConnection)
  {
    TraceView traceView = mergeTraceConnection(startConnection);
    ConnectionView connection;
    if (traceView != null)
    {
      connection = traceView.getStartConnection();
    }
    else
    {
      connection = startConnection;
    }
    return connectConnections(connection);
  }

  private TraceView mergeTraceConnection(ConnectionView startConnection)
  {
    List<ComponentView> connectedComponents = startConnection.getConnectedComponents();
    TraceView traceView1 = null;
    TraceView traceView2 = null;
    if (connectedComponents.size() == 2)
    {
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          if (traceView1 == null)
          {
            traceView1 = (TraceView) connectedComponent;
          }
          else
          {
            traceView2 = (TraceView) connectedComponent;
          }
        }
      }
    }

    if ((traceView1 != null) && (traceView2 != null))
    {
      return mergeTrace(traceView1);
    }
    return null;
  }

  public Set<TraceView> createTraces(Line line)
  {
    splitTrace(line.getStart(), line.getDirection());
    splitTrace(line.getEnd(), line.getDirection());

    List<ConnectionView> sortedConnections = new ArrayList<>(getConnectionsOnLine(line));

    Collections.sort(sortedConnections);

    List<TraceView> result = null;
    if (sortedConnections.size() == 0)
    {
      result = new ArrayList<>();
      result.add(new TraceView(this, line.getStart(), line.getEnd()));
    }
    else
    {
      if (line.isEastWest())
      {
        List<Int2D> positions = getEastWestPositions(line, sortedConnections);
        result = createTracesBetweenEmptyPositions(positions);
      }
      else if (line.isNorthSouth())
      {
        List<Int2D> positions = getNorthSouthPositions(line, sortedConnections);
        result = createTracesBetweenEmptyPositions(positions);
      }
    }

    Set<TraceView> mergedTraces = new LinkedHashSet<>();
    if ((null != result) && (result.size() >= 1))
    {
      for (TraceView traceView : result)
      {
        TraceView mergedTrace = mergeTrace(traceView);
        mergedTraces.add(mergedTrace);
      }
    }
    return mergedTraces;
  }

  private List<TraceView> createTracesBetweenEmptyPositions(List<Int2D> positions)
  {
    List<TraceView> result = new ArrayList<>();
    for (int i = 0; i < positions.size() - 1; i++)
    {
      Int2D start = positions.get(i);
      Int2D end = positions.get(i + 1);
      List<TraceOverlap> tracesOverlapping = getTracesOverlapping(new Line(start, end));
      if (tracesOverlapping.size() == 0)
      {
        TraceView traceView = new TraceView(this, start, end);
        result.add(traceView);
      }
    }
    return result;
  }

  private List<Int2D> getEastWestPositions(Line line, List<ConnectionView> sortedConnections)
  {
    int minimumX = line.getMinimumX();
    int maximumX = line.getMaximumX();
    int y = line.getY();

    Int2D start = new Int2D(minimumX, y);
    Int2D end = new Int2D(maximumX, y);
    return getPositions(sortedConnections, start, end);
  }

  private List<Int2D> getNorthSouthPositions(Line line, List<ConnectionView> sortedConnections)
  {
    int minimumY = line.getMinimumY();
    int maximumY = line.getMaximumY();
    int x = line.getX();

    Int2D start = new Int2D(x, minimumY);
    Int2D end = new Int2D(x, maximumY);
    return getPositions(sortedConnections, start, end);
  }

  private List<Int2D> getPositions(List<ConnectionView> sortedConnections, Int2D start, Int2D end)
  {
    List<Int2D> positions = new ArrayList<>();

    ConnectionView firstConnection = sortedConnections.get(0);
    if (!firstConnection.getGridPosition().equals(start))
    {
      positions.add(start);
    }

    for (ConnectionView sortedConnection : sortedConnections)
    {
      positions.add(sortedConnection.getGridPosition());
    }

    Int2D lastPosition = positions.get(positions.size() - 1);
    if (!end.equals(lastPosition))
    {
      positions.add(end);
    }
    return positions;
  }

  private TraceView getExactTraceView(Int2D start, Int2D end)
  {
    ConnectionView startConnection = getConnection(start);
    if (startConnection == null)
    {
      return null;
    }
    ConnectionView endConnection = getConnection(end);
    if (endConnection == null)
    {
      return null;
    }

    Set<TraceView> traceViews = new HashSet<>();
    for (ComponentView connectedComponent : startConnection.getConnectedComponents())
    {
      if (connectedComponent instanceof TraceView)
      {
        traceViews.add((TraceView) connectedComponent);
      }
    }
    for (ComponentView connectedComponent : endConnection.getConnectedComponents())
    {
      if (connectedComponent instanceof TraceView)
      {
        if (traceViews.contains(connectedComponent))
        {
          return (TraceView) connectedComponent;
        }
      }
    }
    return null;
  }

  private Set<ConnectionView> getConnectionsOnLine(Line line)
  {
    Set<ConnectionView> notStraightConnections = new LinkedHashSet<>();
    notStraightConnections.addAll(getDiscretePortConnectionsOnLine(line));
    notStraightConnections.addAll(getTraceConnectionsOnLine(line));
    return notStraightConnections;
  }

  private Set<ConnectionView> getDiscretePortConnectionsOnLine(Line line)
  {
    Set<ConnectionView> connectionViews = new LinkedHashSet<>();
    for (DiscreteView discreteView : discreteViews)
    {
      List<PortView> portViews = discreteView.getPorts();
      for (PortView portView : portViews)
      {
        ConnectionView connection = portView.getConnection();
        if (line.isPositionOn(connection.getGridPosition()))
        {
          connectionViews.add(connection);
        }
      }
    }

    return connectionViews;
  }

  private Set<ConnectionView> getTraceConnectionsOnLine(Line line)
  {
    Set<ConnectionView> notStraightConnections = new LinkedHashSet<>();
    for (TraceView traceView : traceViews)
    {
      if (line.isPositionOn(traceView.getStartPosition()))
      {
        notStraightConnections.add(traceView.getStartConnection());
      }
      if (line.isPositionOn(traceView.getEndPosition()))
      {
        notStraightConnections.add(traceView.getEndConnection());
      }
    }
    return notStraightConnections;
  }

  public void validateConsistency()
  {
    for (TraceView traceView : traceViews)
    {
      ConnectionView startConnection = traceView.getStartConnection();
      for (ComponentView componentView : startConnection.getConnectedComponents())
      {
        if (componentView instanceof DiscreteView)
        {
          if (!discreteViews.contains(componentView))
          {
            throw new SimulatorException("Discrete component [" + componentView.getDescription() + "] referenced by trace [" + traceView.getDescription() + "].");
          }
        }

        if (componentView instanceof TraceView)
        {
          if (!traceViews.contains(componentView))
          {
            throw new SimulatorException("Trace [" + componentView.getDescription() + "] referenced by trace [" + traceView.getDescription() + "].");
          }
        }
      }
    }
  }

  public CircuitData save()
  {
    ArrayList<DiscreteData> discreteDatas = new ArrayList<>();
    for (DiscreteView discreteView : discreteViews)
    {
      DiscreteData discreteData = discreteView.save();
      if (discreteData == null)
      {
        throw new SimulatorException("%s [%s] save may not return null.", discreteView.getClass().getSimpleName(), discreteView.getName());
      }

      discreteDatas.add(discreteData);
    }

    ArrayList<TraceData> traceDatas = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceData traceData = traceView.save();
      traceDatas.add(traceData);
    }

    TimelineData timelineData = simulation.getTimeline().save();
    return new CircuitData(timelineData,
                           discreteDatas,
                           traceDatas);
  }

  public void load(CircuitData circuitData)
  {
    simulation.getTimeline().load(circuitData.timeline);

    TraceLoader traceLoader = new TraceLoader();

    for (TraceData traceData : circuitData.traces)
    {
      traceData.create(this, traceLoader);
    }

    for (DiscreteData discreteData : circuitData.integratedCircuits)
    {
      discreteData.createAndLoad(this, traceLoader);
    }
  }

  public Set<PortView> createAndConnectDiscreteView(DiscreteView discreteView)
  {
    Set<PortView> updatedPortViews = new LinkedHashSet<>();
    List<PortView> ports = discreteView.getPorts();
    for (PortView portView : ports)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = getOrAddConnection(portPosition, discreteView);
      portView.setConnection(connectionView);
      updatedPortViews.addAll(connectConnections(connectionView));
    }
    discreteView.enable(simulation);
    discreteView.simulationStarted(simulation);

    return updatedPortViews;
  }

  public void createConnectionViews(DiscreteView discreteView)
  {
    List<PortView> ports = discreteView.getPorts();
    for (PortView portView : ports)
    {
      Int2D portPosition = portView.getGridPosition();
      ConnectionView connectionView = getOrAddConnection(portPosition, discreteView);
      portView.setConnection(connectionView);
    }
    discreteView.enable(simulation);
  }

  public void fireConnectionEvents(Set<PortView> updatedPortViews)
  {
    for (PortView portView : updatedPortViews)
    {
      Port port = portView.getPort();
      port.traceConnected(simulation, port);
    }
  }

  public Timeline getTimeline()
  {
    return simulation.getTimeline();
  }

  public VoltageRepresentation getColours()
  {
    return colours;
  }

  public Simulation getSimulation()
  {
    return simulation;
  }


}

