package raft.test;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import raft.Raft;
import raft.RaftNodeGrpc;
import raft.cuhk.RaftClient;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.*;

public class test {


    public static void main(String[] args) throws InterruptedException {

        CommandLine cli = loadSystemArgs(args);
        RaftClient raftClient = null;
        if (cli.hasOption("d")){
            String[] hosts = cli.getOptionValue("d").split(":");
            if (hosts.length != 2){
                System.out.println("Illegal address...exit");
                return;
            }
            raftClient = getRaftClient(hosts[0], Integer.parseInt(hosts[1]));
            System.out.println("connect to node["+hosts[0]+":"+hosts[1]+"] successful...");
        }else if (cli.hasOption("s")){
            String[] hosts = cli.getOptionValue("s").split(",");
            String[] node = hosts[0].split(":");
            if (node.length != 2){
                System.out.println("Illegal address...exit");
                return;
            }
            raftClient = getRaftClient(node[0], Integer.parseInt(node[1]));
            String msg = raftClient.whoAreYou().getMsg();
            String[] split = msg.split(">>");
            String[] leader = split[1].split(":");
            raftClient.shutdown();
            raftClient = getRaftClient(leader[0], Integer.parseInt(leader[1]));
            System.out.println("connect to leader["+leader[0]+":"+leader[1]+"] successful...");
        }
        if (raftClient == null){
            System.out.println("client is null...exit");
            return;
        }
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String input = sc.nextLine();
            if ("exit".equals(input) || "quit".equals(input)){
                break;
            }
            if (input.startsWith("put")){
                String[] ss = input.split(" ");
                if (ss.length != 3){
                    System.out.println("unknown query");
                    continue;
                }
                Raft.ProposeReply reply = raftClient.putValue(ss[1], Integer.parseInt(ss[2]));
                System.out.println(reply.getStatus());

            }else if (input.startsWith("get")) {
                String[] ss = input.split(" ");
                if (ss.length != 2){
                    System.out.println("unknown query");
                    continue;
                }
                Raft.GetValueReply reply = raftClient.getValue(ss[1]);
                System.out.println(reply.getStatus() + " | "+reply.getV());

            }else if (input.startsWith("delete")){
                String[] ss = input.split(" ");
                if (ss.length != 2){
                    System.out.println("unknown query");
                    continue;
                }
                Raft.ProposeReply reply = raftClient.deleteValue(ss[1]);
                System.out.println(reply.getStatus());

            }else {
                System.out.println("unknown query");
                continue;
            }

        }
        raftClient.shutdown();
        System.out.println("bye~");
    }

    public static RaftClient getRaftClient(String host, int port) {
        RaftClient client = new RaftClient(host, port);
        return client;
    }

    public static CommandLine loadSystemArgs(String[] args){
        Option opt1 = new Option("s","servers",true,"add operation");
        opt1.setRequired(false);
        Option opt2 = new Option("d","host",true,"subbstract operation");
        opt2.setRequired(false);
        Options options = new Options();
        options.addOption(opt1);
        options.addOption(opt2);
        CommandLine cli = null;
        CommandLineParser cliParser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            cli = cliParser.parse(options, args);
        } catch (ParseException e) {
            // 解析失败是用 HelpFormatter 打印 帮助信息
            helpFormatter.printHelp(">>>>>> test cli options", options);
            e.printStackTrace();
        }
        return cli;
    }

}
