# RL Learning

A personal workspace for learning and experimenting with reinforcement learning. The repository currently starts with a hands-on deep RL notebook and can grow with future notes, exercises, and experiments.

## Current material

### Unit 1 — Train your first Deep RL agent

[`notebooks/unit1/unit1.ipynb`](notebooks/unit1/unit1.ipynb) follows the Hugging Face Deep Reinforcement Learning Course Unit 1 tutorial. It trains a PPO agent to land the LunarLander environment using Gymnasium and Stable-Baselines3, evaluates the agent, and demonstrates saving and sharing a trained model through the Hugging Face Hub.

The notebook retains its original course content and attribution. It is designed to run in Google Colab with a GPU runtime; some cells install system packages or restart the runtime, so follow the notebook from top to bottom.

## Open in Google Colab

Open the notebook from this repository in Colab, then select **Runtime → Change runtime type → T4 GPU** if GPU training is desired. Training and Hugging Face Hub upload are optional; skip the login and upload cells if you only want to run the lesson locally in Colab.

## Repository layout

```text
notebooks/
└── unit1/
    └── unit1.ipynb
```

Future learning material should be grouped by course unit or topic under `notebooks/`, with concise notes or experiment code added alongside it when useful.

## Source

The Unit 1 notebook is based on the [Hugging Face Deep RL Course](https://github.com/huggingface/deep-rl-class). See the notebook for its full tutorial text, links, and attribution.
