#include <linux/module.h>
#include <linux/kernel.h>
#include <net/tcp.h>

void tcp_reno_init(struct sock *sk){
    tcp_sk(sk)->snd_ssthresh = TCP_INFINITE_SSTHRESH;
    tcp_sk(sk)->snd_cwnd = 2;
}

u32 tcp_reno_ssthresh(struct sock *sk){
    const struct tcp_sock *tp = tcp_sk(sk);
        
    u32 ssthresh_modified = tp->snd_cwnd >> 1U;
    
    if(ssthresh_modified > 2U){
        ssthresh_modified += (ssthresh_modified >> 1U);
    }
    else{
        ssthresh_modified += 1U;
    }

    return max(ssthresh_modified, 2U);
}

void tcp_reno_cong_avoid(struct sock *sk, u32 ack, u32 acked){
    struct tcp_sock *tp = tcp_sk(sk);

    if (!tcp_is_cwnd_limited(sk))
        return;

    if (tp->snd_cwnd <= tp->snd_ssthresh) {
        acked = tcp_slow_start(tp, acked);
        if (!acked)
            return;
    } 
    else{
        u32 diff = tp->snd_cwnd_cnt / tp->snd_cwnd;

        if(tp->snd_cwnd < 2 * tp->snd_ssthresh){
            tp->snd_cwnd_cnt += (acked + 1);
        }
        else {
            tp->snd_cwnd_cnt += acked;
        }

        if(tp->snd_cwnd_cnt >= tp->snd_cwnd){
            tp->snd_cwnd_cnt -= diff * tp->snd_cwnd;
            tp->snd_cwnd += diff;
        }
    }
    
    tp->snd_cwnd = min(tp->snd_cwnd, tp->snd_cwnd_clamp);
}

u32 tcp_reno_undo_cwnd(struct sock *sk){
    return tcp_sk(sk)->snd_cwnd;
}

static struct tcp_congestion_ops tcp_reno_custom = {
    .init           = tcp_reno_init,
    .ssthresh       = tcp_reno_ssthresh,
    .cong_avoid     = tcp_reno_cong_avoid,
    .undo_cwnd      = tcp_reno_undo_cwnd,

    .owner          = THIS_MODULE,
    .name           = "reno_custom",
};

static int __init tcp_reno_module_init(void){
    BUILD_BUG_ON(sizeof(struct tcp_congestion_ops) != sizeof(struct tcp_congestion_ops));
    
    if (tcp_register_congestion_control(&tcp_reno_custom))
        return -ENOBUFS;

    return 0;
}

static void __exit tcp_reno_module_exit(void){
    tcp_unregister_congestion_control(&tcp_reno_custom);
}

module_init(tcp_reno_module_init);
module_exit(tcp_reno_module_exit);

MODULE_AUTHOR("nethw");
MODULE_LICENSE("GPL");
MODULE_DESCRIPTION("TCP Reno Congestion Control");
