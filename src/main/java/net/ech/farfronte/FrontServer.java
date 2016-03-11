package net.ech.farfronte;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Handles a server-side channel.
 */
public class FrontServer {

  private int port;

  public FrontServer(int port) {
    this.port = port;
  }

  public void run() throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      new ServerBootstrap()
       .group(bossGroup, workerGroup)
       .channel(NioServerSocketChannel.class)
       .option(ChannelOption.SO_BACKLOG, 128)          // make this configurable
       .childOption(ChannelOption.SO_KEEPALIVE, true)  // and this
       .childHandler(new FrontServerInitializer()) 
       .bind(port)
         .sync().channel().closeFuture().sync();
    }
    finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws Exception {
    int port = Integer.parseInt(System.getProperty("port", "9090"));
    System.out.println("Server listening on port " + port);
    new FrontServer(port).run();
  }
}
